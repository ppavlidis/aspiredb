<script type="text/javascript" src="dwr/engine.js"></script>

<script src="dwr/interface/HomeController.js"></script>



<script src="/aspiredb/scripts/lib/ext-all-debug-w-comments.js"></script>
	

<script>
	HomeController.getTestValueObject( {
		callback : function(testvo) {
			alert(testvo.name);
		}
	});

</script>

<html>
<body>
	<h1>HOME</h1>
	
	<div id="testwidget" align="left"></div>
</body>
</html>