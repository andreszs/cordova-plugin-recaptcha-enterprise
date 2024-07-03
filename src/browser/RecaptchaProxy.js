var RecaptchaProxy = {
    verify: function(successCallback, errorCallback, args) {
      var sitekey = '';
      var badge = 'bottomright';
      var theme = 'light';
      var size = 'invisible';

      if (args && typeof (args[0]) === 'object') {
        if (typeof (args[0].sitekeyWeb) === 'string' && args[0].sitekeyWeb.length > 0) {
          sitekey = args[0].sitekeyWeb;
        } else {
          errorCallback('Verify called without providing sitekeyWeb');
          return;
        }

        if (typeof (args[0].badge) === 'string') {
          badge = args[0].badge;
        }

        if (typeof (args[0].theme) === 'string') {
          theme = args[0].theme;
        }

        if (typeof (args[0].size) === 'string') {
          size = args[0].size;
        }
      } else {
        errorCallback('Invalid arguments');
        return;
      }

      function loadRecaptchaScript(callback) {
        if (typeof grecaptcha !== 'undefined') {
          callback();
          return;
        }

        var script = document.createElement('script');
        script.src = 'https://www.google.com/recaptcha/enterprise.js';
        script.async = true;
        script.defer = true;
        script.onload = callback;
        script.onerror = function() {
          errorCallback('Failed to load reCAPTCHA script');
        };
        document.head.appendChild(script);
      }

      loadRecaptchaScript(function() {
        grecaptcha.enterprise.ready(function() {
          // Create a temporary container for the reCAPTCHA widget
          var tempContainer = document.createElement('div');
          document.body.appendChild(tempContainer);

          try {
            var widgetId = grecaptcha.enterprise.render(tempContainer, {
              'sitekey': sitekey,
              'size': size,
              'badge': badge,
              'theme': theme,
              'callback': function(token) {
                // Clean up the temporary container before calling the success callback
                document.body.removeChild(tempContainer);
                if (typeof successCallback === 'function') {
                  successCallback(token);
                }
              },
              'error-callback': function(error) {
                console.log(error);
                // Clean up the temporary container
                document.body.removeChild(tempContainer);
                if (typeof errorCallback === 'function') {
                  errorCallback(error);
                }
              }
            });

            // Execute the reCAPTCHA challenge
            grecaptcha.enterprise.execute(widgetId);
          } catch (error) {
            // Clean up the temporary container
            document.body.removeChild(tempContainer);
            if (typeof errorCallback === 'function') {
              errorCallback(error.message || 'An error occurred during reCAPTCHA execution');
            }
          }
        });
      });
    }
};

module.exports = RecaptchaProxy;

require('cordova/exec/proxy').add('RecaptchaEnterprise', RecaptchaProxy);
