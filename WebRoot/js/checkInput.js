$(document).ready(function(){
	$("input").change(function(){
		var pattern = new RegExp("[`~!@#$^&*()=\\s|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]");
		if(pattern.test($(this).val())){
			alert("不能输入非法字符！");
			$(this).val("");
		}
	})
})