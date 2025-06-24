document.addEventListener('DOMContentLoaded', function () {
    const vagaSelect = document.getElementById('vagaSelect');
    const candidatoSelect = document.getElementById('candidatoSelect');
    if (vagaSelect && candidatoSelect) {
        vagaSelect.addEventListener('change', function () {
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
                            data.forEach(function (candidatura) {
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

    // Submissão do formulário de nova entrevista
    var formNova = document.getElementById('formNovaEntrevista');
    if (formNova) {
        formNova.addEventListener('submit', function (e) {
            var btnSalvar = document.getElementById('btn-salvar-nova-entrevista');
            if (btnSalvar) {
                btnSalvar.disabled = true;
                btnSalvar.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Enviando...';
            }
            // O formulário será enviado normalmente (POST padrão), então restaura o botão após um tempo caso haja erro
            setTimeout(function () {
                if (btnSalvar) {
                    btnSalvar.disabled = false;
                    btnSalvar.innerHTML = 'Salvar';
                }
            }, 500); // fallback de 5s caso não haja resposta
        });
    }

    // Função para atualizar o histórico de comunicações
    function atualizarHistoricoComunicacoes(entrevistaId) {
        fetch(`/entrevista/${entrevistaId}/comunicacoes-html`)
            .then(res => res.text())
            .then(html => {
                document.getElementById('comunicacoes-historico').innerHTML = html;
                // Reatribui eventos aos botões de comunicação após atualizar o HTML
                atribuirEventosComunicacao();
            });
    }

    // Função para atribuir eventos aos botões de comunicação
    function atribuirEventosComunicacao() {
        document.querySelectorAll('.btn-comunicar-candidato').forEach(function (btn) {
            btn.addEventListener('click', function (e) {
                e.preventDefault();
                document.getElementById('comunicarEmail').value = this.getAttribute('data-email');
                document.getElementById('comunicarEntrevistaId').value = this.getAttribute('data-entrevista-id');
                document.getElementById('comunicarNomeSpan').textContent = this.getAttribute('data-nome');
                document.getElementById('comunicacaoId').value = this.getAttribute('data-comunicacao-id');
                if (this.hasAttribute('data-mensagem')) {
                    document.getElementById('comunicarMensagem').value = this.getAttribute('data-mensagem');
                } else {
                    document.getElementById('comunicarMensagem').value = '';
                }
                var modalEl = document.getElementById('comunicarCandidatoModal');
                var modal = bootstrap.Modal.getOrCreateInstance(modalEl);
                modal.show();
            });
        });
    }

    // Inicializa eventos ao carregar a página
    atribuirEventosComunicacao();

    var formComunicar = document.getElementById('formComunicarCandidato');
    if (formComunicar) {
        formComunicar.addEventListener('submit', function (e) {
            const botaoEnviar = document.getElementById('btn-enviar-mensagem');
            botaoEnviar.disabled = true;
            botaoEnviar.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Enviando...';
            e.preventDefault();
            const email = document.getElementById('comunicarEmail').value;
            const mensagem = document.getElementById('comunicarMensagem').value;
            const entrevistaId = document.getElementById('comunicarEntrevistaId').value;
            const comunicacaoId = document.getElementById('comunicacaoId').value;
            fetch('/comunicacao/comunicar-candidato', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({comunicacaoId, email, mensagem, entrevistaId})
            })
                .then(res => res.json())
                .then(resp => {
                    botaoEnviar.innerHTML = 'Enviar';
                    var modalEl = document.getElementById('comunicarCandidatoModal');
                    var modal = bootstrap.Modal.getInstance(modalEl);
                    modal.hide();
                    if (resp.sucesso) {
                        exibirMensagemValidacao('Mensagem enviada com sucesso!', 'success');
                        // Só atualiza o histórico após o modal fechar completamente
                        modalEl.addEventListener('hidden.bs.modal', function handler() {
                            atualizarHistoricoComunicacoes(entrevistaId);
                            modalEl.removeEventListener('hidden.bs.modal', handler);
                        });
                    } else {
                        exibirMensagemValidacao(resp.mensagem || '', 'danger');
                    }
                });
        });
    }
});
