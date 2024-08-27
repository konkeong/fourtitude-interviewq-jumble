(function ($) {
    "use strict";

    // Spinner
    var spinner = function () {
        setTimeout(function () {
            if ($('#spinner').length > 0) {
                $('#spinner').removeClass('show');
            }
        }, 1);
    };
    spinner();


    // Back to top button
    $(window).scroll(function () {
        if ($(this).scrollTop() > 300) {
            $('.back-to-top').fadeIn('slow');
        } else {
            $('.back-to-top').fadeOut('slow');
        }
    });
    $('.back-to-top').click(function () {
        $('html, body').animate({scrollTop: 0}, 1500, 'easeInOutExpo');
        return false;
    });


    // Sidebar Toggler
    $('.sidebar-toggler').click(function () {
        $('.sidebar, .content').toggleClass("open");
        return false;
    });


    // Progress Bar
    $('.pg-bar').waypoint(function () {
        $('.progress .progress-bar').each(function () {
            $(this).css("width", $(this).attr("aria-valuenow") + '%');
        });
    }, {offset: '80%'});


    // Calender
    $('#calender').datetimepicker({
        inline: true,
        format: 'L'
    });


    $(document).ready(function () {
        const locPathname = $(location).attr('pathname');
        if (locPathname === '/game/new') {
            $('#mnuItmGameNew').addClass('active');
            $('#mnuDivGame').addClass('show');
        } else if (locPathname === '/game/play') {
            $('#mnuItmGamePlay').addClass('active');
            $('#mnuDivGame').addClass('show');
        } else if (locPathname === '/game/help') {
            $('#mnuItmGameHelp').addClass('active');
            $('#mnuDivGame').addClass('show');
        } else if (locPathname === '/scramble') {
            $('#mnuItmScramble').addClass('active');
            $('#mnuDivRoot').addClass('show');
        } else if (locPathname === '/palindrome') {
            $('#mnuItmPalindrome').addClass('active');
            $('#mnuDivRoot').addClass('show');
        } else if (locPathname === '/exists') {
            $('#mnuItmExists').addClass('active');
            $('#mnuDivRoot').addClass('show');
        } else if (locPathname === '/prefix') {
            $('#mnuItmPrefix').addClass('active');
            $('#mnuDivRoot').addClass('show');
        } else if (locPathname === '/search') {
            $('#mnuItmSearch').addClass('active');
            $('#mnuDivRoot').addClass('show');
        } else if (locPathname === '/subWords') {
            $('#mnuItmSubWords').addClass('active');
            $('#mnuDivRoot').addClass('show');
        } else {
            $('#mnuItmHome').addClass('active');
        }
    });
})(jQuery);

