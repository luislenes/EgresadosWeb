$(document).ready(function () {
    $('.f-invitado').attr('disabled', 'disabled');
    $('.f-personal').attr('disabled', 'disabled');
    $('.f-egresado').attr('disabled', 'disabled');
    $('.f-admin').removeAttr('disabled');
    $('#txt-name').attr('placeholder', 'Ejemplo: Juan Perez');

    $('.f-invitado').removeAttr('required');
    $('.f-personal').removeAttr('required');
    $('.f-egresado').removeAttr('required');
    $('.f-admin').attr('required', 'required');

    $('#code-message').removeAttr('class');
    $('span#code-message').text('');

    $('#capaEgresado').slideUp();
    $('#capaEmpresa').slideUp();

    $('select#slt-type').on('change', function () {
        var value = $(this).val();
        if (value === "administrador") {
            $('.f-invitado').attr('disabled', 'disabled');
            $('.f-personal').attr('disabled', 'disabled');
            $('.f-egresado').attr('disabled', 'disabled');
            $('.f-admin').removeAttr('disabled');
            $('#txt-name').attr('placeholder', 'Ejemplo: Juan Perez');

            $('.f-invitado').removeAttr('required');
            $('.f-personal').removeAttr('required');
            $('.f-egresado').removeAttr('required');
            $('.f-admin').attr('required', 'required');

            $('#capaEgresado').slideUp();
            $('#capaEmpresa').slideUp();
        } else if (value === "egresado") {
            $('.f-invitado').attr('disabled', 'disabled');
            $('.f-admin').attr('disabled', 'disabled');
            $('.f-personal').removeAttr('disabled');
            $('.f-egresado').removeAttr('disabled');
            $('#txt-name').attr('placeholder', 'Primer Nombre');

            $('.f-invitado').removeAttr('required');
            $('.f-admin').removeAttr('required');
            $('.f-personal').attr('required', 'required');
            $('.f-egresado').attr('required', 'required');

            $('#capaEgresado').slideDown();
            $('#capaEmpresa').slideUp();
        } else {
            $('.f-egresado').attr('disabled', 'disabled');
            $('.f-admin').attr('disabled', 'disabled');
            $('.f-personal').removeAttr('disabled');
            $('.f-invitado').removeAttr('disabled');
            $('#txt-name').attr('placeholder', 'Primer Nombre');

            $('.f-egresado').removeAttr('required');
            $('.f-admin').removeAttr('required');
            $('.f-personal').attr('required', 'required');
            $('.f-invitado').attr('required', 'required');

            $('#capaEgresado').slideUp();
            $('#capaEmpresa').slideDown();
        }
    });

    $('#txt-code').keyup(function () {
        var value = $('#txt-code').val();

        if (value !== "") {
            $.ajax({
                beforeSend: function () {
                    $('#code-message').text('Comprobando disponibilidad...');
                    $('#code-message').removeAttr('class');
                },
                url: 'webresources/service/codeCheckAvailability',
                type: 'GET',
                dataType: 'xml',
                data: {
                    code: value
                },
                success: function (response) {
                    console.log(response);
                    
                    var availability = $(response).find('availability').text();
                    if (availability === 'true') {
                        $('#code-message').text(' Disponible.');
                        $('#code-message').attr('class', 'icon-good color-success');
                    } else {
                        $('#code-message').text(' No disponible.');
                        $('#code-message').attr('class', 'icon-error color-error');
                    }
                },
                error: function (jqXHR, status, errorThrown) {
                    console.log(status);
                    console.log(errorThrown);
                }
            });
        } else {
            $('#code-message').text('');
            $('#code-message').removeAttr('class');
        }

    });
});