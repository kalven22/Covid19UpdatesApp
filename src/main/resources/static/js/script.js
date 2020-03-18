$(document).ready(function(){
    $("#globalCard").click(function(){
        $("#globalTable").show();
        $("#canadaTable").hide();
        $("#usaTable").hide();
        $("#indiaTable").hide();
    });
    $("#canadaCard").click(function(){
        $("#globalTable").hide();
        $("#canadaTable").show()
        $("#usaTable").hide();
        $("#indiaTable").hide();
    });
    $("#usaCard").click(function(){
        $("#globalTable").hide();
        $("#canadaTable").hide();
        $("#usaTable").show();
        $("#indiaTable").hide();
    });
    $("#indiaCard").click(function(){
        $("#globalTable").hide();
        $("#canadaTable").hide();
        $("#usaTable").hide();
        $("#indiaTable").show();
    });

});

