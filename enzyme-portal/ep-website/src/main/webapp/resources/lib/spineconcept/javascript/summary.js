$(function() {
	$('.selection ul').css('position', 'absolute').hide();
	$('.references table').hide();
	if ($('.selection').length > 0) {
		$('.classification .box').append('<div class="trigger"></div>');
	}
	// Click events
	$('.references .button').click(function(e) {
		$('.selection ul').hide();
		$('.references table').toggle();
		e.stopPropagation();
	});
	$('.classification').click(function(e) {
		$('.selection ul').toggle();
		$('.references table').hide();
		e.stopPropagation();
	});
	$(document).click(function(){
		$('.references table').hide();
		$('.selection ul').hide();
	});
	$('.selection .cardinality').parents('a').click(function() {
		$('.selection ul').hide();
		var dialogBox = $('<div class="multi_reference_selection"></div>')
		.html('Loading references...')
		.dialog({
			modal: true,
			width: '800px',
			minHeight: 300,
			maxHeight: 400,
			position: ['center', 'center'],
			title: 'Multiple references'
		});

		var href = $(this).attr('href');
		$.get(href, function(data) {
			$('.multi_reference_selection').html($('.content', data).html());
		});
		return false;
	});
	$('.references table tr').click(function(e) {
		document.location = $('a', this).attr('href');
	});
	// Network tooltips
	$('.networks').append('<div class="tooltip"></div>');
	var tooltip = $('.networks .tooltip');
	$('.networks ul li').each(function() {
		var child = $(this).children().first();
		var title = child.attr('title');
		if (title) {
			child.removeAttr('title');
			$(this).mouseover(function() {
				tooltip.text(title);
				tooltip.show();
			}).mouseout(function(){
				tooltip.text('');
				tooltip.hide();
			});
		}
	});
	$('.print').attr('target', '_blank');
	$('#more-molecule-trigger').click(function(){
		$('#more-molecule-container').toggle();
		$(this).toggle();
		return false;
	});
});
