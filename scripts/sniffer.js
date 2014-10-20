angular.module('sniffer', ['ngRoute', 'firebase','ngTable'])
 
.value('fbURL', 'https://popping-fire-8932.firebaseio.com/')
 
.factory('Applications', function($firebase, fbURL) {
  return $firebase(new Firebase(fbURL));
})
 
.config(function($routeProvider) {
  $routeProvider
    .when('/', {
      controller:'ListCtrl',
      templateUrl:'list.html'
    })
    .when('/edit/:applicationId', {
      controller:'EditCtrl',
      templateUrl:'detail.html'
    })
    .when('/new', {
      controller:'CreateCtrl',
      templateUrl:'detail.html'
    })
    .otherwise({
      redirectTo:'/'
    });
})
.service('AnalysisService', function(){
  this.parseResponseHeaders = function(headerStr) {
    var headers = [];
    if (!headerStr) {
      return headers;
    }
    var headerPairs = headerStr.split('\u000d\u000a');
    for (var i = 0; i < headerPairs.length; i++) {
      var headerPair = headerPairs[i];
      // Can't use split() here because it does the wrong thing
      // if the header value has the string ": " in it.
      var index = headerPair.indexOf('\u003a\u0020');
      if (index > 0) {
        var key = headerPair.substring(0, index);
        var val = headerPair.substring(index + 2);
        var header = {}
        header.name = key;
        header.value = val;
        headers[i] = header;
      }
    }
    return headers;
  }
  this.knownHeaders = {
    'x-powered-by': {
      // 'Ruby on Rails': /Phusion Passenger/,
      'Express.js': /Express/,
      'PHP': /PHP\/?(.*)/,
      'Dinkly': /DINKLY\/?(.*)/,
      'ASP.NET': /ASP\.NET/,
      'Nette': /Nette Framework/
    },
    'server': {
      'Apache': /Apache\/?(.*)/,
      'nginx': /nginx\/?(.*)/,
      'IIS': /Microsoft-IIS\/?(.*)/,
      'BWS': /BWS\/?(.*)/
    },
    'via': {
      'Varnish': /(.*) varnish/
    }
  };

  this.analysisHeader = function(xhr){
    var appsFound = [];
    var headers = this.parseResponseHeaders(xhr.getAllResponseHeaders());
    for (var i = headers.length - 1; i >= 0; i--) {
      var apps = this.knownHeaders[headers[i].name.toLowerCase()];
      if (!apps) {
        continue;
      }
      for (var app in apps) {
        var matches = headers[i].value.match(apps[app]);
        if (matches) {
          var version = matches[1] || -1;
          appsFound[app] = version;
        }
      }
    }

    //var poweredBy = xhr.getResponseHeader('x-powered-by');
    return appsFound;
  }
})
.controller('ListCtrl', function($scope, Applications, ngTableParams, AnalysisService) {
  $scope.applications = Applications;
  $scope.applications.$on("loaded", function() {
    setTimeout(function(){
      var keys = $scope.applications.$getIndex();
      keys.forEach(function(key, i) {
        var targetUrl = $scope.applications[key].site;
        var ajaxStartTime= new Date().getTime();
        $.ajax({
          type:"get",
          url: targetUrl,
          complete: function(xhr, status){
            // Update the RPC status
            $scope.applications[key].status = status;
            // Analysis the response
            var ajaxEndTime = new Date().getTime();
            //$scope.applications[key].start = ajaxStartTime;
            //$scope.applications[key].end = ajaxEndTime;
            $scope.applications[key].latency = ajaxEndTime - ajaxStartTime;
            if(xhr.status == 0 || xhr.status == 404){
              $scope.applications[key].latency = -1;
            }

            $scope.applications[key].server = xhr.getResponseHeader('server');

            //console.log(AnalysisService.analysisHeader(xhr));
            var pattern = new RegExp("<script src=\"(.*)\">.*</script>")
            console.log(xhr.responseText.match(pattern));


            $scope.$digest();
          }
        });
      });
    },3000);
  });
})
 
.controller('CreateCtrl', function($scope, $location, $timeout, Applications) {
  $scope.save = function() {
    Applications.$add($scope.application, function() {
      $timeout(function() { $location.path('/'); });
    });
  };
})
 
.controller('EditCtrl',
  function($scope, $location, $routeParams, $firebase, fbURL) {
    var applicationUrl = fbURL + $routeParams.applicationId;
    $scope.application = $firebase(new Firebase(applicationUrl));
 
    $scope.destroy = function() {
      $scope.application.$remove();
      $location.path('/');
    };
 
    $scope.save = function() {
      $scope.application.$save();
      $location.path('/');
    };
});