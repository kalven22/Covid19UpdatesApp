function funVisible(){
    document.getElementById("globalTable").style.display = "none";
}

$(document).ready(function(){
    $("#firstcard").click(function(){
        $("#globalTable").toggle();
    });
});

