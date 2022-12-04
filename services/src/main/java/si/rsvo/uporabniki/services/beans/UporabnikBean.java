package si.rsvo.uporabniki.services.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;

import org.eclipse.microprofile.metrics.annotation.Timed;
import si.rsvo.uporabniki.lib.Uporabnik;
import si.rsvo.uporabniki.models.converters.UporabnikConverter;
import si.rsvo.uporabniki.models.entities.UporabnikEntity;


@RequestScoped
public class UporabnikBean {

    private Logger log = Logger.getLogger(UporabnikBean.class.getName());

    @Inject
    private EntityManager em;

    public List<Uporabnik> getUporabniki() {

        TypedQuery<UporabnikEntity> query = em.createNamedQuery(
                "UporabnikEntity.getAll", UporabnikEntity.class);

        List<UporabnikEntity> resultList = query.getResultList();

        return resultList.stream().map(UporabnikConverter::toDto).collect(Collectors.toList());

    }

    /*
    @Timed
    public List<UporabnikoviIzdelkiMetadata> getUporabnikoviIzdelkiMetadataFilter(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, UporabnikoviIzdelkiMetadataEntity.class, queryParameters).stream()
                .map(UporabnikoviIzdelkiMetadataConverter::toDto).collect(Collectors.toList());
    }

    public UporabnikoviIzdelkiMetadata getUporabnikoviIzdelkiMetadata(Integer id) {

        UporabnikoviIzdelkiMetadataEntity uporabnikoviIzdelkiMetadataEntity = em.find(UporabnikoviIzdelkiMetadataEntity.class, id);

        if (uporabnikoviIzdelkiMetadataEntity == null) {
            throw new NotFoundException();
        }

        UporabnikoviIzdelkiMetadata uporabnikoviIzdelkiMetadata = UporabnikoviIzdelkiMetadataConverter.toDto(uporabnikoviIzdelkiMetadataEntity);

        return uporabnikoviIzdelkiMetadata;
    }

    public UporabnikoviIzdelkiMetadata createUporabnikoviIzdelkiMetadata(UporabnikoviIzdelkiMetadata uporabnikoviIzdelkiMetadata) {

        UporabnikoviIzdelkiMetadataEntity uporabnikoviIzdelkiMetadataEntity = UporabnikoviIzdelkiMetadataConverter.toEntity(uporabnikoviIzdelkiMetadata);

        try {
            beginTx();
            em.persist(uporabnikoviIzdelkiMetadataEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        if (uporabnikoviIzdelkiMetadataEntity.getIzdelekId() == null || uporabnikoviIzdelkiMetadataEntity.getUporabnikId() == null) {
            throw new RuntimeException("Entity was not persisted");
        }

        return UporabnikoviIzdelkiMetadataConverter.toDto(uporabnikoviIzdelkiMetadataEntity);
    }

    public UporabnikoviIzdelkiMetadata putUporabnikoviIzdelkiMetadata(Integer id, UporabnikoviIzdelkiMetadata uporabnikoviIzdelkiMetadata) {
uporabniki
        UporabnikoviIzdelkiMetadataEntity c = em.find(UporabnikoviIzdelkiMetadataEntity.class, id);

        if (c == null) {
            return null;
        }

        UporabnikoviIzdelkiMetadataEntity updatedUporabnikoviIzdelkiMetadataEntity = UporabnikoviIzdelkiMetadataConverter.toEntity(uporabnikoviIzdelkiMetadata);

        try {
            beginTx();
            updatedUporabnikoviIzdelkiMetadataEntity.setIzdelekId(c.getIzdelekId());
            updatedUporabnikoviIzdelkiMetadataEntity.setUporabnikId(c.getUporabnikId());
            updatedUporabnikoviIzdelkiMetadataEntity = em.merge(updatedUporabnikoviIzdelkiMetadataEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        return UporabnikoviIzdelkiMetadataConverter.toDto(updatedUporabnikoviIzdelkiMetadataEntity);
    }

    public boolean deleteUporabnikoviIzdelkiMetadata(Integer id) {

        UporabnikoviIzdelkiMetadataEntity uporabnikoviIzdelkiMetadata = em.find(UporabnikoviIzdelkiMetadataEntity.class, id);

        if (uporabnikoviIzdelkiMetadata != null) {
            try {
                beginTx();
                em.remove(uporabnikoviIzdelkiMetadata);
                commitTx();
            }
            catch (Exception e) {
                rollbackTx();
            }
        }
        else {
            return false;
        }

        return true;
    }

     */

    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}
