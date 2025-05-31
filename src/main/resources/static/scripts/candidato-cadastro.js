document.getElementById('btnSalvar').addEventListener('click', function () {
  document.getElementById('formSalvar').submit();
});

// Máscaras para campos
IMask(
  document.getElementById('telefone'),
  {
    mask: '(00) 00000-0000',
    lazy: false
  }
)
IMask(
  document.getElementById('cpf'),
  {
    mask: '000.000.000-00',
    lazy: false
  }
)
IMask(
  document.getElementById('rg'),
  {
    mask: '00.000.000-0',
    lazy: false
  }
)
IMask(
  document.getElementById('cep'),
  {
    mask: '00000-000',
    lazy: false
  }
)

// Gerenciamento de habilidades
document.addEventListener('DOMContentLoaded', function() {
  const habilidadesContainer = document.getElementById('habilidades-container');
  const addHabilidadeButton = document.getElementById('adicionar-habilidade');

  if (addHabilidadeButton) {
    addHabilidadeButton.addEventListener('click', function() {
      const index = habilidadesContainer.querySelectorAll('.habilidade-item').length;
      const template = `
        <div class="habilidade-item mb-2">
          <div class="row">
            <div class="col-md-6">
              <input type="text" class="form-control" name="habilidades[${index}].nome" 
                     placeholder="Nome da habilidade" required>
              <input type="hidden" name="habilidades[${index}].candidato.id" value="${document.getElementById('formSalvar').elements['id'].value}">
            </div>
            <div class="col-md-3">
              <select class="form-select" name="habilidades[${index}].nivel">
                <option value="1">Nível 1 - Básico</option>
                <option value="2">Nível 2 - Intermediário</option>
                <option value="3">Nível 3 - Avançado</option>
                <option value="4">Nível 4 - Proficiente</option>
                <option value="5">Nível 5 - Especialista</option>
              </select>
            </div>
            <div class="col-md-3">
              <button type="button" class="btn btn-danger btn-sm remover-habilidade">Remover</button>
            </div>
          </div>
        </div>
      `;

      const tempDiv = document.createElement('div');
      tempDiv.innerHTML = template;
      habilidadesContainer.appendChild(tempDiv.firstElementChild);

      // Adicionar evento para o novo botão de remover
      const newRemoveButton = habilidadesContainer.querySelector('.habilidade-item:last-child .remover-habilidade');
      newRemoveButton.addEventListener('click', removeHabilidade);
    });

    // Adicionar eventos para botões de remover existentes
    const removeHabilidadeButtons = document.querySelectorAll('.remover-habilidade');
    removeHabilidadeButtons.forEach(button => {
      button.addEventListener('click', removeHabilidade);
    });

    function removeHabilidade() {
      this.closest('.habilidade-item').remove();
      // Reindexar os campos para manter a sequência correta
      const items = habilidadesContainer.querySelectorAll('.habilidade-item');
      items.forEach((item, index) => {
        const inputs = item.querySelectorAll('input, select');
        inputs.forEach(input => {
          const name = input.getAttribute('name');
          if (name) {
            // Não reindexar campos de ID para preservar a referência
            if (!name.includes('.id')) {
              const newName = name.replace(/habilidades\[\d+\]/, `habilidades[${index}]`);
              input.setAttribute('name', newName);
            }
          }
        });
      });
    }
  }

  // Gerenciamento de experiências
  const experienciasContainer = document.getElementById('experiencias-container');
  const addExperienciaButton = document.getElementById('adicionar-experiencia');

  if (addExperienciaButton) {
    addExperienciaButton.addEventListener('click', function() {
      const index = experienciasContainer.querySelectorAll('.experiencia-item').length;
      const template = `
        <div class="experiencia-item mb-3">
          <div class="row mb-2">
            <div class="col-md-5">
              <input type="text" class="form-control" name="experiencias[${index}].empresa" 
                     placeholder="Nome da empresa" required>
              <input type="hidden" name="experiencias[${index}].candidato.id" value="${document.getElementById('formSalvar').elements['id'].value}">
            </div>
            <div class="col-md-4">
              <input type="text" class="form-control" name="experiencias[${index}].cargo" 
                     placeholder="Cargo" required>
            </div>
            <div class="col-md-3">
              <button type="button" class="btn btn-danger btn-sm remover-experiencia">Remover</button>
            </div>
          </div>
          <div class="row mb-2">
            <div class="col-md-4">
              <label class="form-label">Data de Início:</label>
              <input type="date" class="form-control" name="experiencias[${index}].dataInicio" required>
            </div>
            <div class="col-md-4">
              <label class="form-label">Data de Término:</label>
              <input type="date" class="form-control data-fim" name="experiencias[${index}].dataFim">
            </div>
            <div class="col-md-4">
              <div class="form-check mt-4">
                <input class="form-check-input atual-checkbox" type="checkbox" 
                       name="experiencias[${index}].atual" value="true" data-index="${index}">
                <input type="hidden" name="experiencias[${index}].atual" value="false">
                <label class="form-check-label">Emprego Atual</label>
              </div>
            </div>
          </div>
          <div class="row">
            <div class="col-md-12">
              <textarea class="form-control" name="experiencias[${index}].descricao" 
                        placeholder="Descrição das atividades" rows="2"></textarea>
            </div>
          </div>
          <hr>
        </div>
      `;

      const tempDiv = document.createElement('div');
      tempDiv.innerHTML = template;
      experienciasContainer.appendChild(tempDiv.firstElementChild);

      // Adicionar evento para o novo botão de remover
      const newRemoveButton = experienciasContainer.querySelector('.experiencia-item:last-child .remover-experiencia');
      newRemoveButton.addEventListener('click', removeExperiencia);

      // Adicionar evento para o novo checkbox
      const newCheckbox = experienciasContainer.querySelector('.experiencia-item:last-child .atual-checkbox');
      newCheckbox.addEventListener('change', toggleDataFim);
    });

    // Adicionar eventos para botões de remover existentes
    const removeExperienciaButtons = document.querySelectorAll('.remover-experiencia');
    removeExperienciaButtons.forEach(button => {
      button.addEventListener('click', removeExperiencia);
    });

    // Adicionar eventos para checkboxes existentes
    const atualCheckboxes = document.querySelectorAll('.atual-checkbox');
    atualCheckboxes.forEach(checkbox => {
      checkbox.addEventListener('change', toggleDataFim);
    });

    function removeExperiencia() {
      this.closest('.experiencia-item').remove();
      // Reindexar os campos para manter a sequência correta
      const items = experienciasContainer.querySelectorAll('.experiencia-item');
      items.forEach((item, index) => {
        const inputs = item.querySelectorAll('input, select, textarea');
        inputs.forEach(input => {
          const name = input.getAttribute('name');
          if (name) {
            // Não reindexar campos de ID para preservar a referência
            if (!name.includes('.id')) {
              const newName = name.replace(/experiencias\[\d+\]/, `experiencias[${index}]`);
              input.setAttribute('name', newName);
            }
          }

          const dataIndex = input.getAttribute('data-index');
          if (dataIndex) {
            input.setAttribute('data-index', index);
          }
        });
      });
    }

    function toggleDataFim() {
      const index = this.getAttribute('data-index');
      const dataFimInput = this.closest('.experiencia-item').querySelector('.data-fim');
      dataFimInput.disabled = this.checked;
    }
  }

  // Gerenciamento de formações
  const formacoesContainer = document.getElementById('formacoes-container');
  const addFormacaoButton = document.getElementById('adicionar-formacao');

  if (addFormacaoButton) {
    addFormacaoButton.addEventListener('click', function() {
      const index = formacoesContainer.querySelectorAll('.formacao-item').length;
      const template = `
        <div class="formacao-item mb-3">
          <div class="row mb-2">
            <div class="col-md-5">
              <input type="text" class="form-control" name="formacoes[${index}].instituicao" 
                     placeholder="Nome da instituição" required>
              <input type="hidden" name="formacoes[${index}].candidato.id" value="${document.getElementById('formSalvar').elements['id'].value}">
            </div>
            <div class="col-md-4">
              <input type="text" class="form-control" name="formacoes[${index}].curso" 
                     placeholder="Nome do curso" required>
            </div>
            <div class="col-md-3">
              <button type="button" class="btn btn-danger btn-sm remover-formacao">Remover</button>
            </div>
          </div>
          <div class="row mb-2">
            <div class="col-md-4">
              <label class="form-label">Nível:</label>
              <select class="form-select" name="formacoes[${index}].nivel">
                <option value="Ensino Médio">Ensino Médio</option>
                <option value="Técnico">Técnico</option>
                <option value="Graduação">Graduação</option>
                <option value="Pós-graduação">Pós-graduação</option>
                <option value="Mestrado">Mestrado</option>
                <option value="Doutorado">Doutorado</option>
              </select>
            </div>
            <div class="col-md-4">
              <label class="form-label">Data de Início:</label>
              <input type="date" class="form-control" name="formacoes[${index}].dataInicio" required>
            </div>
            <div class="col-md-4">
              <label class="form-label">Data de Conclusão:</label>
              <input type="date" class="form-control data-fim" name="formacoes[${index}].dataFim" disabled>
            </div>
          </div>
          <div class="row">
            <div class="col-md-12">
              <div class="form-check">
                <input class="form-check-input concluido-checkbox" type="checkbox" 
                       name="formacoes[${index}].concluido" value="true" data-index="${index}">
                <input type="hidden" name="formacoes[${index}].concluido" value="false">
                <label class="form-check-label">Concluído</label>
              </div>
            </div>
          </div>
          <hr>
        </div>
      `;

      const tempDiv = document.createElement('div');
      tempDiv.innerHTML = template;
      formacoesContainer.appendChild(tempDiv.firstElementChild);

      // Adicionar evento para o novo botão de remover
      const newRemoveButton = formacoesContainer.querySelector('.formacao-item:last-child .remover-formacao');
      newRemoveButton.addEventListener('click', removeFormacao);

      // Adicionar evento para o novo checkbox
      const newCheckbox = formacoesContainer.querySelector('.formacao-item:last-child .concluido-checkbox');
      newCheckbox.addEventListener('change', toggleDataFimFormacao);
    });

    // Adicionar eventos para botões de remover existentes
    const removeFormacaoButtons = document.querySelectorAll('.remover-formacao');
    removeFormacaoButtons.forEach(button => {
      button.addEventListener('click', removeFormacao);
    });

    // Adicionar eventos para checkboxes existentes
    const concluidoCheckboxes = document.querySelectorAll('.concluido-checkbox');
    concluidoCheckboxes.forEach(checkbox => {
      checkbox.addEventListener('change', toggleDataFimFormacao);
    });

    function removeFormacao() {
      this.closest('.formacao-item').remove();
      // Reindexar os campos para manter a sequência correta
      const items = formacoesContainer.querySelectorAll('.formacao-item');
      items.forEach((item, index) => {
        const inputs = item.querySelectorAll('input, select');
        inputs.forEach(input => {
          const name = input.getAttribute('name');
          if (name) {
            // Não reindexar campos de ID para preservar a referência
            if (!name.includes('.id')) {
              const newName = name.replace(/formacoes\[\d+\]/, `formacoes[${index}]`);
              input.setAttribute('name', newName);
            }
          }

          const dataIndex = input.getAttribute('data-index');
          if (dataIndex) {
            input.setAttribute('data-index', index);
          }
        });
      });
    }

    function toggleDataFimFormacao() {
      const index = this.getAttribute('data-index');
      const dataFimInput = this.closest('.formacao-item').querySelector('.data-fim');
      dataFimInput.disabled = !this.checked;
    }
  }
});

