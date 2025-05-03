function mostrarNotificacao(tipo, mensagem, titulo) {
    toastr.options = {
        "closeButton": true,
        "debug": false,
        "newestOnTop": false,
        "progressBar": true,
        "positionClass": "toast-top-right",
        "preventDuplicates": false,
        "onclick": null,
        "showDuration": "300",
        "hideDuration": "1000",
        "timeOut": "5000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    };

    switch (tipo) {
        case 'sucesso':
            toastr.success(mensagem, titulo);
            break;
        case 'erro':
            toastr.error(mensagem, titulo);
            break;
        case 'aviso':
            toastr.warning(mensagem, titulo);
            break;
        case 'info':
            toastr.info(mensagem, titulo);
            break;
        default:
            toastr.info(mensagem, titulo);
    }
}