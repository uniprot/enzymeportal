$(function() {
	$('.references .multi').each(function() {
		$(this).addClass('closed');
		$('a.details', this).click(function() {
			var parent = $(this).parent('.multi');
			if (parent.is('.closed')) {
				parent.removeClass('closed');
			} else {
				parent.addClass('closed');
			}
			return false;
		});
	});
});
