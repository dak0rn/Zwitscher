dom(function(_) {

    var counter = _.element('#count');
    var textarea = _.element('#say-box');
    var addon = " / 145";
    var submit = _.element('#submit');

    // Initialize with current length
    var set = function(event) {
        var len = (event.target.value || '').length;
        counter.innerHTML = len + addon;

        submit.disabled = len > 145 || len === 0;
    };
    set({ target: textarea });

    textarea.addEventListener('keyup', set);
});
