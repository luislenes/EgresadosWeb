function changeTypeUser() {
    $('#form-typeuser').submit();
}

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

$(document).ready(function () {
    var type = $.get("type");
    if (type !== null) {
        $('#slt-type').val(type);
    }
});