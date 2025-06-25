// Script para preencher o modal de entrevista com os dados do candidato

document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.btn-cadastrar-entrevista').forEach(function (btn) {
        btn.addEventListener('click', function () {
            // Pega os dados do botão
            var candidatoId = this.getAttribute('data-candidato-id');
            var candidaturaId = this.getAttribute('data-candidatura-id');
            var candidatoNome = this.getAttribute('data-candidato-nome');
            // Preenche os campos do modal
            document.getElementById('candidatoEntrevistaId').value = candidatoId;
            document.getElementById('candidaturaEntrevistaId').value = candidaturaId;
            document.getElementById('candidatoEntrevista').textContent = candidatoNome;
        });
    });

    // Submissão do formulário de edição
    var formEditar = document.getElementById('formEditarEntrevista');
    if (formEditar) {
        formEditar.addEventListener('submit', function (e) {
            const botaoSalvar = document.getElementById('btn-salvar-entrevista');
            botaoSalvar.disabled = true;
            botaoSalvar.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Salvando...';
            e.preventDefault();
            const id = document.getElementById('editarEntrevistaId').value;
            const local = document.getElementById('editarLocal').value;
            const dataHora = document.getElementById('editarDataHora').value;
            const observacoes = document.getElementById('editarObservacoes').value;
            fetch(`/entrevista/editar`, {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({id, local, dataHora, observacoes})
            })
                .then(res => res.json())
                .then(resp => {
                    if (resp.sucesso) {
                        exibirMensagemValidacao('Entrevista editada com sucesso!', 'success');
                        var modalEl = document.getElementById('editarEntrevistaModal');
                        var modal = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl);
                        modal.hide();
                        setTimeout(() => location.reload(), 2000);
                    } else {
                        exibirMensagemValidacao('Erro ao salvar: ' + (resp.mensagem || ''), 'danger');
                    }
                    setTimeout(function () {
                        botaoSalvar.disabled = false;
                        botaoSalvar.innerHTML = 'Salvar alterações';
                    }, 500); // fallback de 5s caso não haja resposta
                });
        });
    }

    // Botão Editar abre o modal e preenche os campos
    document.querySelectorAll('.btn-editar-entrevista').forEach(function (btn) {
        btn.addEventListener('click', function (e) {
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

    // Preencher modal de edição ao clicar em visualizar entrevista
    document.querySelectorAll('.btn-visualizar-entrevista').forEach(function (btn) {
        btn.addEventListener('click', function (e) {
            e.preventDefault();
            var entrevistaId = this.getAttribute('data-entrevista-id');
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

    // Submissão do formulário de nova entrevista
    var formNova = document.getElementById('formNovaEntrevista');
    if (formNova) {
        formNova.addEventListener('submit', function (e) {
            var btnSalvar = document.getElementById('btn-salvar-nova-entrevista');
            if (btnSalvar) {
                btnSalvar.disabled = true;
                btnSalvar.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Salvando...';
            }
            // O formulário será enviado normalmente (POST padrão), então restaura o botão após um tempo caso haja erro
            setTimeout(function () {
                if (btnSalvar) {
                    btnSalvar.disabled = false;
                    btnSalvar.innerHTML = 'Salvar';
                }
            }, 5000); // fallback de 5s caso não haja resposta
        });
    }

    // Função utilitária para exibir mensagens no fragmento de validação
    function exibirMensagemValidacao(texto, tipo) {
        // tipo: 'success' ou 'danger'
        let container = document.getElementById('div-mensagem');
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
});
