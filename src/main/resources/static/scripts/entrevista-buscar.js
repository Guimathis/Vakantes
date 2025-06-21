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
                    location.reload();
                } else {
                    alert('Erro ao salvar: ' + (resp.mensagem || ''));
                }
            });
        });
    }
});

