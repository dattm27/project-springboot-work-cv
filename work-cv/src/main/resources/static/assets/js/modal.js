function sendApplication(job_id) {
	$.ajax({
		type: 'GET',
		url: '/candidate/apply/' + job_id,
		success: function(response) {
			$('#resultModal .modal-body').html(response);
			$('#resultModal').modal('show');
		},
		error: function() {
			$('#resultModal .modal-body').html('<p>Có lỗi xảy ra khi xử lý yêu cầu.</p>');
			$('#resultModal').modal('show');
		}
	});
}

function hideModal () {
	 // Chọn modal theo ID và gọi phương thức .modal('hide')
    $('#resultModal').modal('hide');
}