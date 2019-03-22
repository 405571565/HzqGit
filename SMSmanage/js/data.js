function init(nowPage){
	$("#loading").show()
	$('#tableData').attr('data-finish',0)
	tableData(nowPage, 0)
	var timer = setInterval(function(){
		if($('#tableData').attr('data-finish')==1){
			$("#page").pagination($('#tableData').attr('data-page'), {
				num_edge_entries: 1,
				num_display_entries: 3,
				items_per_page: 1,
				prev_text: '上一页',
				next_text: '下一页',
				current_page: nowPage-1,
				callback: function(cur) {
					if($('#tableData').attr('data-repeat')==0){
						$('#tableData').attr('data-repeat',1)
					}else{
						$("#loading").show()
						tableData(cur + 1,1);
					}
				}
			});
			clearInterval(timer)
		}
	},200)
}
init(1)
$("#pageBtn").click(function() {
	var cur = $(this).prev().val();
	$("#page").pagination($('#tableData').attr('data-page'), {
		num_edge_entries: 1,
		num_display_entries: 3,
		items_per_page: 1,
		prev_text: '上一页',
		next_text: '下一页',
		current_page: cur - 1,
		callback: function(rescur) {
			$("#loading").show()
			tableData(rescur + 1,1);
		}
	});
})
$("#searchBtn").click(function(){
	init(1)
})
function delPage(reduce){
	var nowPage;
	for(var i=0;i<$("#page .current").length;i++){
		var pagestr = $("#page .current").eq(i).text()
		if(pagestr!='上一页'&&pagestr!='下一页'){
			nowPage = pagestr;
			break
		}
	}
	if($("#tableData tr").length==2){
		if(reduce!=1){
			nowPage = parseInt(nowPage)-1
			$("#tableData").attr('data-page',parseInt($("tableData").attr('data-page'))-1)
		}
	}
	return nowPage
}
$(".return").click(function(){
	$('.popupCon').css('display','none')
})