$(document).ready(function () {
    $('.message').hide();
    
    $(document.updateProfile).submit(function (e) {
        e.preventDefault();
        
        $.ajax({
            url: "updateProfile",
            type: 'POST',
            dataType: 'json',
            data: $(document.updateProfile).serialize(),
            success: function (response) {
                console.log(response);
                
                $('.message p').text(response.message);
                $('.message div').first().attr('class', response.icon);
                $('.message').show();
            },
            error: function (jqXHR, status, error) {
                console.error(status);
                console.error(error);
            }
        });
    });
    
    $(document.changePass).submit(function (e) {
        e.preventDefault();
        
        $.ajax({
            url: "updatePassword",
            type: 'POST',
            dataType: 'json',
            data: $(document.changePass).serialize(),
            success: function (response) {
                console.log(response);
                
                $('.message p').text(response.message);
                $('.message div').first().attr('class', response.icon);
                $('.message').show();
            },
            error: function (jqXHR, status, error) {
                console.error(status);
                console.error(error);
            }
        });
    });
});