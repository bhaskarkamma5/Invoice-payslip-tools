
$(document).ready(function() {
	alert("I'm active");
  $('#first_form').submit(function(e) {
    e.preventDefault();
    var ename = $('#empname').val();
    var eid = $('#empid').val();
    var dojo = $('#doj').val();
    var sal = $('#salary').val();
    
    
    
    $(".error").remove();
 
    if (ename.length < 1) {
      $('#empname').after('<span class="error">please enter name</span>');
    }
    if (eid.length < 1) {
      $('#empid').after('<span class="error">please enter ID</span>');
    }
    if (dojo.length < 1) {
      $('#doj').after('<span class="error">enter joining date</span>');
    } else {
      var regEx = /^([0-9]{4})-([0-9]{2})-([0-9]{2})$/;
      var validDoj = regEx.test(dojo);
      if (!validDoj) {
        $('#doj').after('<span class="error">Invalid Date format</span>');
      }
    }
    if (sal.length < 1) {
      $('#salary').after('<span class="error">please enter salary</span>');
    }
    var error = 0;
    var deptno = $('#deptno').val();

    if (deptno == '0') {
        error = 1;
        alert('You should select a Department.');
    }

    if (error) {
        return false;
    } else {
        return true;
    }
 
  });
 
});
