document.addEventListener('DOMContentLoaded', function() {
    const vagaSelect = document.getElementById('vagaSelect');
    const candidatoSelect = document.getElementById('candidatoSelect');
    if (vagaSelect && candidatoSelect) {
        vagaSelect.addEventListener('change', function() {
            const vagaId = this.value;
            candidatoSelect.innerHTML = '<option>Carregando...</option>';
            if (vagaId) {
                fetch(`/entrevista/candidatos?vagaId=${vagaId}`)
                    .then(response => response.json())
                    .then(data => {
                        candidatoSelect.innerHTML = '';
                        if (data.length === 0) {
                            candidatoSelect.innerHTML = '<option value="">Nenhum candidato para esta vaga</option>';
                        } else {
                            candidatoSelect.innerHTML = '<option value="">Selecione um candidato</option>';
                            candidatoSelect.disabled = false;
                            data.forEach(function(candidatura) {
                                candidatoSelect.innerHTML += `<option value="${candidatura.id}">${candidatura.nomeCandidato}</option>`;
                            });
                        }
                    });
            } else {
                candidatoSelect.innerHTML = '<option value="">Selecione uma vaga</option>';
            }
        });
    }
    // Botão Editar abre o modal e preenche os campos
    document.querySelectorAll('.btn-editar-entrevista').forEach(function(btn) {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            const entrevistaId = this.getAttribute('data-entrevista-id');
            fetch(`/entrevista/detalhes-json/${entrevistaId}`)
                .then(res => res.json())
                .then(data => {
                    document.getElementById('editarEntrevistaId').value = data.id;
                    document.getElementById('editarLocal').value = data.local;
                    document.getElementById('editarDataHora').value = data.dataHora.replace(' ', 'T');
                    document.getElementById('editarObservacoes').value = data.observacoes || '';
                    var modal = new bootstrap.Modal(document.getElementById('editarEntrevistaModal'));
                    modal.show();
                });
        });
    });
    // Função utilitária para exibir mensagens no fragmento de validação
    function exibirMensagemValidacao(texto, tipo) {
        // tipo: 'success' ou 'danger'
        let container = document.querySelector('.msgValidacao');
        if (!container) {
            // Se não existe, cria no topo do .card-body
            const cardBody = document.getElementById('tb-entrevistas');
            container = document.createElement('div');
            container.className = 'alert alert-' + tipo + ' alert-dismissible msgValidacao';
            container.setAttribute('role', 'alert');
            cardBody.prepend(container);
        }
        container.className = 'alert alert-' + tipo + ' alert-dismissible msgValidacao';
        container.innerHTML = `
            <span>${texto}</span>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        `;
    }
    // Submissão do formulário de edição
    var formEditar = document.getElementById('formEditarEntrevista');
    if (formEditar) {
        formEditar.addEventListener('submit', function(e) {
            e.preventDefault();
            const id = document.getElementById('editarEntrevistaId').value;
            const local = document.getElementById('editarLocal').value;
            const dataHora = document.getElementById('editarDataHora').value;
            const observacoes = document.getElementById('editarObservacoes').value;
            fetch(`/entrevista/editar`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ id, local, dataHora, observacoes })
            })
            .then(res => res.json())
            .then(resp => {
                if(resp.sucesso) {
                    exibirMensagemValidacao('Entrevista editada com sucesso!', 'success');
                    setTimeout(() => location.reload(), 1200);
                } else {
                    exibirMensagemValidacao('Erro ao salvar: ' + (resp.mensagem || ''), 'danger');
                }
            });
        });
    }
    // Comunicação com candidato
    document.querySelectorAll('.btn-comunicar-candidato').forEach(function(btn) {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            document.getElementById('comunicarEmail').value = this.getAttribute('data-email');
            document.getElementById('comunicarMensagem').value = '';
            var modal = new bootstrap.Modal(document.getElementById('comunicarCandidatoModal'));
            modal.show();
        });
    });

    var formComunicar = document.getElementById('formComunicarCandidato');
    if (formComunicar) {
        formComunicar.addEventListener('submit', function(e) {
            const botaoEnviar = document.getElementById('btn-enviar-mensagem');
            botaoEnviar.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Enviando...';
            e.preventDefault();
            const email = document.getElementById('comunicarEmail').value;
            const mensagem = document.getElementById('comunicarMensagem').value;
            fetch('/entrevista/comunicar-candidato', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, mensagem })
            })
            .then(res => res.json())
            .then(resp => {
                botaoEnviar.innerHTML = 'Enviar';
                if(resp.sucesso) {
                    exibirMensagemValidacao('Mensagem enviada com sucesso!', 'success');
                    var modal = bootstrap.Modal.getInstance(document.getElementById('comunicarCandidatoModal'));
                    modal.hide();
                } else {
                    exibirMensagemValidacao('Erro ao enviar mensagem: ' + (resp.mensagem || ''), 'danger');
                }
            });
        });
    }
});
