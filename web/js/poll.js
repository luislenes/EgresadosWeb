/* global angular */
var app = angular.module('EgresadosModule', []);

app.controller('PollController', function ($scope, $http) {
    $scope.count = -1;
    $scope.message = {};
    $scope.showMessage = false;
    $scope.poll = {
        name: 'Encuesta #' + ($scope.count + 1),
        questions: [{
                content: '',
                options: [],
                multi: false
            }],
        enable: false
    };

    $scope.updateCount = function () {
        $http.get('webresources/service/getCountPoll')
                .success(function (data) {
                    console.log(data);
                    $scope.count = data.count;
                    $scope.poll.name = 'Encuesta #' + ($scope.count + 1);
                })
                .error(function (error) {
                    console.log(error);
                });
    };

    $scope.updateCount();

    $scope.addQuestion = function () {
        var question = {
            content: '',
            options: [],
            multi: false
        };
        $scope.poll.questions.push(question);
    };

    $scope.removeQuestion = function (question) {
        $scope.poll.questions = $scope.poll.questions.filter(function (item) {
            return item !== question;
        });
    };

    $scope.addOption = function (question) {
        question.options.push({option: ''});
    };

    $scope.removeOption = function (question, option) {
        question.options = question.options.filter(function (item) {
            return item !== option;
        });
    };

    $scope.disabledCheck = function (question) {
        if (question.options.length === 0) {
            question.multi = false;
        }
        return question.options.length === 0;
    };

    $scope.submitEditPoll = function () {
        $.ajax({
            url: 'updatePoll',
            type: 'POST',
            dataType: 'json',
            data: {
                doc: JSON.stringify($scope.poll)
            },
            success: function (data) {
                console.log(data);
                $scope.$apply(function () {
                    if (data.state === 'success') {
                        $scope.poll = data.success;
                    }
                    $scope.message = data;
                    $scope.showMessage = true;
                });
            },
            error: function (error) {
                console.log(error);
            }
        });
    };

    $scope.submitCreatePoll = function () {
        $.ajax({
            url: 'addPoll',
            type: 'POST',
            dataType: 'json',
            data: {
                doc: JSON.stringify($scope.poll)
            },
            success: function (data) {
                console.log(data);
                if (data.state === 'success') {
                    $scope.$apply(function () {
                        $scope.updateCount();
                        $scope.poll = {
                            name: 'Encuesta #' + ($scope.count + 1),
                            questions: [{
                                    content: '',
                                    options: [],
                                    multi: false
                                }]
                        };
                    });
                }
                $scope.$apply(function () {
                    $scope.message = data;
                    $scope.showMessage = true;
                });
                console.log($scope);
            },
            error: function (error) {
                console.log(error);
            }
        });
    };
});

app.controller('readPollController', function ($scope, $http) {
    $scope.polls = [];
    $scope.showMessage = false;
    $scope.message = {};

    $http.get('encuesta')
            .success(function (data) {
                console.log(data);
                $scope.polls = data;
            })
            .error(function (error) {
                console.log(error);
            });

    $scope.remove = function (poll) {
        var response = confirm('¿Desea realmente eliminar esta encuesta?\n\nRecuerde que para eliminar una encuesta no debe haber sido publicada ni realizada por los egresados.');
        if (response) {
            //the ajax method DELETE in jquery
            $.ajax({
                url: 'removePoll',
                type: 'post',
                dataType: 'json',
                data: {
                    code: poll.code
                },
                success: function (data) {
                    console.log(data);
                    $scope.$apply(function () {
                        if (data.state === 'success') {
                            $scope.polls = $scope.polls.filter(function (item) {
                                return item !== poll;
                            });
                        }
                        $scope.message = data;
                        $scope.showMessage = true;
                    });
                },
                error: function (error) {
                    console.log(error);
                }
            });
        }
    };

    $scope.enablePoll = function (poll) {
        poll.enable = !poll.enable;
        //the ajax method PUT in jquery
        $.ajax({
            url: 'updatePoll',
            type: 'post',
            dataType: 'json',
            data: {
                doc: JSON.stringify(poll)
            },
            success: function (data) {
                console.log(data);
                $scope.$apply(function () {
                    if (data.state !== 'success') {
                        poll.enable = !poll.enable;
                    }
                    $scope.showMessage = true;
                    $scope.message = data;
                });
            },
            error: function (error) {
                console.log(error);
                $scope.$apply(function () {
                    poll.enable = !poll.enable;
                });
            }
        });
    };
});

app.controller('readEPollController', function ($scope, $http) {
    $scope.polls = [];

    $http.get('encuesta')
            .success(function (data) {
                $scope.polls = data.filter(function (item) {
                    return item.enable;
                });
                $http.get('historialEncuesta')
                        .success(function (data1) {
                            console.log(data1);
                            $scope.polls = $scope.polls.filter(function (item) {
                                for (var i = 0; i < data1.length; i++) {
                                    if (data1[i].poll === item.code) {
                                        return false;
                                    }
                                }
                                return true;
                            });
                            console.log($scope.polls);
                            
                        })
                        .error(function (error) {
                            console.log(error);
                        });
            })
            .error(function (error) {
                console.log(error);
            });
});

app.controller('rePollController', function ($scope, $http) {
    $scope.poll = {};
    $scope.message = {};
    $scope.showMessage = false;
    
    /*
     * {
     *  codePoll: codigo de la encuesta,
     *  answers: vector de respuestas
     * }
     */
    $scope.pollAnswers = {};
    /*
     * {
     *  codoQuestion: codigo de la pregunta,
     *  content: contenido de la encuesta,
     *  option: codigo de la opcion si existe una opcion
     * }
     */
    $scope.pollAnswers.answers = [];
    
    
    var codigo = $.get("codigo");
    $http({
        url: 'encuesta',
        method: 'get',
        params: {
            codigo: codigo
        }
    }).success(function (data) {
        console.log(data);
        $scope.poll = data;
        $scope.pollAnswers.codePoll = $scope.poll.code;  
        for (var i = 0; i < $scope.poll.questions.length; i++) {
            $scope.pollAnswers.answers.push({
                codeQuestion: $scope.poll.questions[i].code,
                content: '',
                option: ''
            });
        }
    }).error(function (error) {
        console.log(error);
    });
    
    $scope.changeOption = function (option, index){
        $scope.pollAnswers.answers[index].option = option.code;
        $scope.pollAnswers.answers[index].content = option.body;
    };
    
    $scope.submitAnswersPoll = function (){
        $.ajax({
            url: 'SaveAnswers',
            type: 'POST',
            dataType: 'json',
            data: {
                doc: JSON.stringify($scope.pollAnswers)
            },
            success: function (data) {
                console.log(data);
                $scope.$apply(function () {
                    $scope.message = data;
                    $scope.showMessage = true;
                });
                window.location = "encuestas.jsp";
                console.log($scope);
            },
            error: function (error) {
                console.log(error);
            }
        });
    };
    
    
});

app.controller('hisPollController', function($scope, $http){
    $scope.history = [];
    
    $http.get('historialEncuesta')
            .success(function (data) {
                console.log(data);
                $scope.history = data;
                console.log($scope.history);

            })
            .error(function (error) {
                console.log(error);
            });
});

app.controller('closeAnswersController', function($scope, $http){
    $scope.polls = [];

    $http.get('encuesta')
            .success(function (data) {
                console.log(data);
                $scope.polls = data;
            })
            .error(function (error) {
                console.log(error);
            });
});

app.controller('questionController', function($scope, $http){
    $scope.questions = [];
    var codigo = $.get('codigo');

    $http.get('preguntasAbiertas',{
        params: {
            codigo: codigo
        }
    })
            .success(function (data) {
                console.log(data);
                $scope.questions = data;
                
            })
            .error(function (error) {
                console.log(error);
            });
            
    $scope.tab = function(index){
        window.location = 'tab.jsp?code='+$scope.questions[index].code+'&poll='+codigo;
    };
});
app.controller('tabController', function($scope, $http){
    $scope.questions = [];
    var codigo = $.get('code');
    var encuesta = $.get('poll');

    $http.get('respuestasPorTabular',{
        params: {
            code: codigo,
            poll: encuesta
        }
    })
            .success(function (data) {
                console.log(data);
                $scope.questions = data;
                
            })
            .error(function (error) {
                console.log(error);
            });
});

(function ($) {
    $.get = function (key) {
        key = key.replace(/[\[]/, '\\[');
        key = key.replace(/[\]]/, '\\]');
        var pattern = "[\\?&]" + key + "=([^&#]*)";
        var regex = new RegExp(pattern);
        var url = unescape(window.location.href);
        var results = regex.exec(url);
        if (results === null) {
            return null;
        } else {
            return results[1];
        }
    };
})(jQuery);