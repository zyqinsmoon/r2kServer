<%@ page language="java" pageEncoding="UTF-8"%>

	<style>
	.ui-autocomplete-loading {
		background: white url('../images/ui-anim_basic_16x16.gif') right center no-repeat;
	}
	#remote-org-div{
		overflow:auto;
		width:100%;
		margin-top:50px;
	}
	#remote-org { width: 25em; }
	.ui-autocomplete{ border:1px solid #999; margin-top:20px; max-height:400px; overflow-y:auto;}
	.search_ddl{ position:relative; width:300px; height:28px; border:1px solid #555;margin-right:15px;float:right;margin-left:0;}
    .search_ddl input{ border:0; padding-left:5px; width:270px; height:26px;}
    .search_ddl a.ddlarrow{ position:absolute; right:1px; top:2px; display:block; width:24px; height:23px; background:url(../images/ddl_arrow.jpg) no-repeat; cursor:pointer;}
	#remote-search-label{
		font-size:14px;
		width:80px;
		margin-left:15px;
		margin-right:0;
		float:left;
		padding:4px 0;
	}
	.ui-dialog{ overflow:visible;}
	</style>
	<script>
	var orgId = "orgId";
	$(function() {
		function log( message ) {
			$( "<div>" ).text( message ).prependTo( "#log" );
			$( "#log" ).scrollTop( 0 );
		}
		
		$( "#remote-org" ).bind("keyup", function( event ) {
			if( event) {
				if(!$.trim($("#remote-org").val())) {
					$( "#changeOrgDialog" ).dialog({
						height: 200 ,
					    width: 450
					 });
					$("#ui-id-4").hide();
				}
			}
		}).autocomplete({
			source: function( request, response ) {
				if(!$.trim(request.term)) {
					return;
				}
				$.ajax({
					url: "/r2k/org/suggest.do",
					dataType: "jsonp",
					data: {
						featureClass: "P",
						style: "full",
						maxRows: 12,
						name_startsWith: $.trim(request.term)
					},
					success: function( data ) {
						var len = data.orgList.length;
						if(len <= 3) {
							$( "#changeOrgDialog" ).dialog({
								height: 200 ,
							    width: 450
							 });
						}else if(3 < len <= 18) {
							$( "#changeOrgDialog" ).dialog({
							      height: 200+ len*20 ,
							      width: 450
							 });
						}else {
							$( "#changeOrgDialog" ).dialog({
							      height: 600 ,
							      width: 450
							 });
						}
						
						response( $.map( data.orgList, function( item ) {
							return {
								label: item.orgName +  ", " + item.orgId,
								value: item.orgId,
								name:item.orgName
							};
						}));
					}
				});
			},
			minLength: 2,
			select: function( event, ui ) {
				if(ui.item){
					var item = ui.item.value;
					window.location = "/r2k/org/getOrgInfo.do?orgId="+item;
				}
			},
			open: function() {
				$( this ).removeClass( "ui-corner-all" ).addClass( "ui-corner-top" );
				var sWid = $( ".search_ddl" ).width()-5;
				$(".ui-autocomplete").css({"width":sWid});
			},
			close: function() {
				$( this ).removeClass( "ui-corner-top" ).addClass( "ui-corner-all" );
			}
		});
		
	});
	
	</script>

<div class="ui-widget" >
	<div id="remote-org-div" class="ui-front">
	<div id="remote-search-label"><label for="remote-org">请输入机构: </label></div>
	<div class="search_ddl">
	    <input  id="remote-org" />
	    <a class="ddlarrow" id="ddlBtn"></a>
	</div>
	</div>
</div>
