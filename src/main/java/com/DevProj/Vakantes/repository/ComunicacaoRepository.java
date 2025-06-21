package com.DevProj.Vakantes.repository;


import com.DevProj.Vakantes.model.comunicacao.Comunicacao;
import com.DevProj.Vakantes.model.entrevista.Entrevista;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface ComunicacaoRepository extends CrudRepository<Comunicacao, Long> {

    List<Comunicacao> findByEntrevistaOrderByDataComunicacaoDesc(Entrevista entrevista);

    Comunicacao findComunicacaoById(Long id);
}
