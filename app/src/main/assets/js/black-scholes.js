/**
 * Implementacao do BLACK-SCHOLES sem considerar o Dividend Yeld (ganho com os dividendos).
 */
if (!bs) {
	var bs = {
		
		/**
		 * Total days of a year that is not a leap year.
		 */
		DAYS_OF_YEAR: 365,
		
		/**
		 * Convert time from days in years, with 5 decimals precision.
		 *
		 * @param {Number} d The time in days.
		 * @returns {Number} Time from days in years.
		 */
		getDaysInYears: function (d) {
			return Math.round((d / 365) * 100000) / 100000;
		},
		
		/**
		 * Standard normal cumulative distribution function.  The probability is estimated
		 * by expanding the CDF into a series using the first 100 terms.
		 * See {@link http://en.wikipedia.org/wiki/Normal_distribution#Cumulative_distribution_function|Wikipedia page}.
		 *
		 * @param {Number} x The upper bound to integrate over.  This is P{Z <= x} where Z is a standard normal random variable.
		 * @returns {Number} The probability that a standard normal random variable will be less than or equal to x
		 */
		stdNormCDF: function (x) {
			var probability = 0;
			// avoid divergence in the series which happens around +/-8 when summing the
			// first 100 terms
			if (x >= 8) {
				probability = 1;
			} else if (x <= -8) {
				probability = 0;
			} else {
				for (var i = 0; i < 100; i++) {
					probability += (Math.pow(x, 2 * i + 1) / this._doubleFactorial(2 * i + 1));
				}
				probability *= Math.pow(Math.E, -0.5 * Math.pow(x, 2));
				probability /= Math.sqrt(2 * Math.PI);
				probability += 0.5;
			}
			return probability;
		},

		/**
		 * Double factorial.  See {@link http://en.wikipedia.org/wiki/Double_factorial|Wikipedia page}.
		 * @private
		 *
		 * @param {Number} n The number to calculate the double factorial of
		 * @returns {Number} The double factorial of n
		 */
		_doubleFactorial: function (n) {
			var val = 1;
			for (var i = n; i > 1; i -= 2) {
				val *= i;
			}
			return val;
		},

		/**
		 * Black-Scholes option pricing formula.
		 * See {@link http://en.wikipedia.org/wiki/Black%E2%80%93Scholes_model#Black-Scholes_formula|Wikipedia page}
		 * for pricing puts in addition to calls.
		 *
		 * @param   {Number} s       Current price of the underlying
		 * @param   {Number} k       Strike price
		 * @param   {Number} t       Time to expiration in years
		 * @param   {Number} v       Volatility as a decimal
		 * @param   {Number} r       Anual risk-free interest rate as a decimal
		 * @param   {String} callPut The type of option to be priced - "call" or "put"
		 * @returns {Number}         Price of the option
		 */
		blackScholes: function (s, k, t, v, r, callPut) {
			var price = null;
			var w = this.getW(s, k, t, v, r);
			if (callPut === "call") {
				price = s * this.stdNormCDF(w) - k * Math.pow(Math.E, -1 * r * t) * this.stdNormCDF(w - v * Math.sqrt(t));
			} else // put
			{
				price = k * Math.pow(Math.E, -1 * r * t) * this.stdNormCDF(v * Math.sqrt(t) - w) - s * this.stdNormCDF(-w);
			}
			return price;
		},
		
		/**
		 * Calculate the volatility of a price option.
		 *
		 * @param   {Number} s       Current price of the underlying
		 * @param   {Number} k       Strike price
		 * @param   {Number} t       Time to expiration in years
		 * @param   {Number} r       Anual risk-free interest rate as a decimal
		 * @param   {Number} op      Current price of the option
		 * @param   {String} callPut The type of option to be priced - "call" or "put"
		 * @returns {Number}         Volatility of the price option
		 */
		getVolatility: function (s, k, t, r, op, callPut) {
			var lowerDiff = Number.POSITIVE_INFINITY;
			var finalVolatility = 0.0;
			for (v = 0.0; v < 1.0; v += 0.01) {
				var bsOP = this.blackScholes(s, k, t, v, r, callPut);
				var diff = Math.abs((bsOP - op) / op);
				if (diff < lowerDiff) {
					lowerDiff = diff;
					finalVolatility = v;
				}
			}
			return finalVolatility;
		},

		/**
		 * Calculate omega as defined in the Black-Scholes formula.
		 *
		 * @param   {Number} s Current price of the underlying
		 * @param   {Number} k Strike price
		 * @param   {Number} t Time to expiration in years
		 * @param   {Number} v Volatility as a decimal
		 * @param   {Number} r Anual risk-free interest rate as a decimal
		 * @returns {Number} The value of omega
		 */
		getW: function (s, k, t, v, r) {
			var w = (Math.log(s / k) + r * t + Math.pow(v, 2) * t / 2) / (v * Math.sqrt(t));
			return w;
		},
	
		/** GREEKS *****/
		/**
		 * Standard normal density function.
		 *
		 * @private
		 * @param {Number} x The value to calculate the standard normal density of
		 * @returns {Number} The value of the standard normal density function at x
		 */
		_stdNormDensity: function (x) {
			return Math.pow(Math.E, -1 * Math.pow(x, 2) / 2) / Math.sqrt(2 * Math.PI);
		},

		/**
		 * Calculates the delta of an option.
		 *
		 * @param {Number} s Current price of the underlying
		 * @param {Number} k Strike price
		 * @param {Number} t Time to expiration in years
		 * @param {Number} v Volatility as a decimal
		 * @param {Number} r Anual risk-free interest rate as a decimal
		 * @param {String} callPut The type of option - "call" or "put"
		 * @returns {Number} The delta of the option
		 */
		getDelta: function (s, k, t, v, r, callPut) {
			if (callPut === "call") {
				return this._callDelta(s, k, t, v, r);
			} else // put
			{
				return this._putDelta(s, k, t, v, r);
			}
		},

		/**
		 * Calculates the delta of a call option.
		 *
		 * @private
		 * @param {Number} s Current price of the underlying
		 * @param {Number} k Strike price
		 * @param {Number} t Time to expiration in years
		 * @param {Number} v Volatility as a decimal
		 * @param {Number} r Anual risk-free interest rate as a decimal
		 * @returns {Number} The delta of the call option
		 */
		_callDelta: function (s, k, t, v, r) {
			var w = this.getW(s, k, t, v, r);
			var delta = null;
			if (!isFinite(w)) {
				delta = (s > k) ? 1 : 0;
			} else {
				delta = this.stdNormCDF(w);
			}
			return delta;
		},

		/**
		 * Calculates the delta of a put option.
		 *
		 * @private
		 * @param {Number} s Current price of the underlying
		 * @param {Number} k Strike price
		 * @param {Number} t Time to expiration in years
		 * @param {Number} v Volatility as a decimal
		 * @param {Number} r Anual risk-free interest rate as a decimal
		 * @returns {Number} The delta of the put option
		 */
		_putDelta: function (s, k, t, v, r) {
			var delta = this._callDelta(s, k, t, v, r) - 1;
			return (delta == -1 && k == s) ? 0 : delta;
		},

		/**
		 * Calculates the rho of an option.
		 *
		 * @param {Number} s Current price of the underlying
		 * @param {Number} k Strike price
		 * @param {Number} t Time to expiration in years
		 * @param {Number} v Volatility as a decimal
		 * @param {Number} r Anual risk-free interest rate as a decimal
		 * @param {String} callPut The type of option - "call" or "put"
		 * @param {String} [scale=100] The value to scale rho by (100=100BPS=1%, 10000=1BPS=.01%)
		 * @returns {Number} The rho of the option
		 */
		getRho: function (s, k, t, v, r, callPut, scale) {
			scale = scale || 100;
			if (callPut === "call") {
				return this._callRho(s, k, t, v, r) / scale;
			} else // put
			{
				return this._putRho(s, k, t, v, r) / scale;
			}
		},

		/**
		 * Calculates the rho of a call option.
		 *
		 * @private
		 * @param {Number} s Current price of the underlying
		 * @param {Number} k Strike price
		 * @param {Number} t Time to expiration in years
		 * @param {Number} v Volatility as a decimal
		 * @param {Number} r Anual risk-free interest rate as a decimal
		 * @returns {Number} The rho of the call option
		 */
		_callRho: function (s, k, t, v, r) {
			var w = this.getW(s, k, t, v, r);
			if (!isNaN(w)) {
				return k * t * Math.pow(Math.E, -1 * r * t) * this.stdNormCDF(w - v * Math.sqrt(t));
			} else {
				return 0;
			}
		},

		/**
		 * Calculates the rho of a put option.
		 *
		 * @private
		 * @param {Number} s Current price of the underlying
		 * @param {Number} k Strike price
		 * @param {Number} t Time to expiration in years
		 * @param {Number} v Volatility as a decimal
		 * @param {Number} r Anual risk-free interest rate as a decimal
		 * @returns {Number} The rho of the put option
		 */
		_putRho: function (s, k, t, v, r) {
			var w = this.getW(s, k, t, v, r);
			if (!isNaN(w)) {
				return -1 * k * t * Math.pow(Math.E, -1 * r * t) * this.stdNormCDF(v * Math.sqrt(t) - w);
			} else {
				return 0;
			}
		},

		/**
		 * Calculates the vega of a call and put option.
		 *
		 * @param {Number} s Current price of the underlying
		 * @param {Number} k Strike price
		 * @param {Number} t Time to expiration in years
		 * @param {Number} v Volatility as a decimal
		 * @param {Number} r Anual risk-free interest rate as a decimal
		 * @returns {Number} The vega of the option
		 */
		getVega: function (s, k, t, v, r) {
			var w = this.getW(s, k, t, v, r);
			return (isFinite(w)) ? (s * Math.sqrt(t) * this._stdNormDensity(w) / 100) : 0;
		},

		/**
		 * Calculates the theta of an option.
		 *
		 * @param {Number} s Current price of the underlying
		 * @param {Number} k Strike price
		 * @param {Number} t Time to expiration in years
		 * @param {Number} v Volatility as a decimal
		 * @param {Number} r Anual risk-free interest rate as a decimal
		 * @param {String} callPut The type of option - "call" or "put"
		 * @returns {Number} The theta of the option
		 */
		getTheta: function (s, k, t, v, r, callPut) {
			if (callPut === "call") {
				return this._callTheta(s, k, t, v, r);
			} else // put
			{
				return this._putTheta(s, k, t, v, r);
			}
		},

		/**
		 * Calculates the theta of a call option.
		 *
		 * @private
		 * @param {Number} s Current price of the underlying
		 * @param {Number} k Strike price
		 * @param {Number} t Time to expiration in years
		 * @param {Number} v Volatility as a decimal
		 * @param {Number} r Anual risk-free interest rate as a decimal
		 * @returns {Number} The theta of the call option
		 */
		_callTheta: function (s, k, t, v, r) {
			var w = this.getW(s, k, t, v, r);
			if (isFinite(w)) {
				//var aux1 = -1 * v * s * this._stdNormDensity(w) / (2 * Math.sqrt(t)) - k * r * Math.pow(Math.E, -1 * r * t) * this.stdNormCDF(w - v * Math.sqrt(t));
				//return aux1;
				return -1 * v * s * this._stdNormDensity(w) / (2 * Math.sqrt(t)) - k * r * Math.pow(Math.E, -1 * r * t) * this.stdNormCDF(w - v * Math.sqrt(t));
				
			} else {
				return 0;
			}
		},

		/**
		 * Calculates the theta of a put option.
		 *
		 * @private
		 * @param {Number} s Current price of the underlying
		 * @param {Number} k Strike price
		 * @param {Number} t Time to expiration in years
		 * @param {Number} v Volatility as a decimal
		 * @param {Number} r Anual risk-free interest rate as a decimal
		 * @returns {Number} The theta of the put option
		 */
		_putTheta: function (s, k, t, v, r) {
			var w = this.getW(s, k, t, v, r);
			if (isFinite(w)) {
				return -1 * v * s * this._stdNormDensity(w) / (2 * Math.sqrt(t)) + k * r * Math.pow(Math.E, -1 * r * t) * this.stdNormCDF(v * Math.sqrt(t) - w);
			} else {
				return 0;
			}
		},

		/**
		 * Calculates the gamma of a call and put option.
		 *
		 * @param {Number} s Current price of the underlying
		 * @param {Number} k Strike price
		 * @param {Number} t Time to expiration in years
		 * @param {Number} v Volatility as a decimal
		 * @param {Number} r Anual risk-free interest rate as a decimal
		 * @returns {Number} The gamma of the option
		 */
		getGamma: function (s, k, t, v, r) {
			var w = this.getW(s, k, t, v, r);
			return (isFinite(w)) ? (this._stdNormDensity(w) / (s * v * Math.sqrt(t))) : 0;
		},
		
		/**
		 * Calculates the VI (intrinsic value) of an option.
		 *
		 * @param {Number} s Current price of the underlying
		 * @param {Number} k Strike price
		 * @param {String} callPut The type of option - "call" or "put"
		 * @returns {Number} The VI of the option
		 */
		getVI: function(s, k, callPut) {
			if (callPut === "call") // call
				return (s >= k) ? s - k : 0.0;
			else // put
				return (s <= k) ? k - s : 0.0;
		},
		
		/**
		 * Calculates the VE (extrinsic value) of an option.
		 *
		 * @param {Number} s Current price of the underlying
		 * @param {Number} k Strike price
		 * @param {Number} op Current price of the option
		 * @param {String} callPut The type of option - "call" or "put"
		 * @returns {Number} The VE of the option
		 */
		getVE: function(s, k, op, callPut) {
			var VI = this.getVI(s, k, callPut);
			return op - VI;
		},
		
		/**
		 * Calculates the time to expiration in years.
		 *
		 * @param {Number} d days to expiration
		 * @returns {Number} The time to expiration in years
		 */
		getTimeToExp: function(d) {
			return (d / this.DAYS_OF_YEAR);
		}
	}
}
