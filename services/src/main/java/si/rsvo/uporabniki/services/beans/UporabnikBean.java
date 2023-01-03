package si.rsvo.uporabniki.services.beans;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.eclipse.microprofile.metrics.annotation.Timed;
import si.rsvo.uporabniki.lib.Uporabnik;
import si.rsvo.uporabniki.models.converters.UporabnikConverter;
import si.rsvo.uporabniki.models.entities.UporabnikEntity;
import si.rsvo.uporabniki.services.util.Celebrity;


@RequestScoped
public class UporabnikBean {

    private Logger log = Logger.getLogger(UporabnikBean.class.getName());

    @Inject
    private EntityManager em;

    private Client httpClient;

    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient();
    }

    public List<Uporabnik> getUporabniki() {

        TypedQuery<UporabnikEntity> query = em.createNamedQuery(
                "UporabnikEntity.getAll", UporabnikEntity.class);

        List<UporabnikEntity> resultList = query.getResultList();

        return resultList.stream().map(UporabnikConverter::toDto).collect(Collectors.toList());

    }

    public Uporabnik createUporabnik(Uporabnik uporabnik) throws IOException {

        UporabnikEntity uporabnikEntity = UporabnikConverter.toEntity(uporabnik);


        // call API check for celebrity name

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet("https://api.api-ninjas.com/v1/celebrity?min_net_worth=1");
        request.setHeader("X-Api-Key", "PHrxDsBI1fm20+0JSR4Eiw==1Ll1KdcxqDcKICPR");
        HttpResponse response = client.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity());

        ObjectMapper mapper = new ObjectMapper();
        Celebrity[] celebrities = mapper.readValue(responseBody, Celebrity[].class);

        for (Celebrity celebrity : celebrities) {
            System.out.println(celebrity.getName());
        }



        try {
            beginTx();
            em.persist(uporabnikEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        if (uporabnikEntity.getId() == null) {
            throw new RuntimeException("Entity was not persisted");
        }


        return UporabnikConverter.toDto(uporabnikEntity);

    }

    public Uporabnik getUporabnikById(Integer id) {

        UporabnikEntity uporabnikEntity = em.find(UporabnikEntity.class, id);

        if (uporabnikEntity == null) {
            throw new NotFoundException();
        }

        Uporabnik uporabnik = UporabnikConverter.toDto(uporabnikEntity);

        return uporabnik;
    }


    @Timed
    public List<Uporabnik> getUporabnikByUsername(String username) {

        TypedQuery<UporabnikEntity> query = em.createNamedQuery(
                "UporabnikEntity.getByUsername", UporabnikEntity.class);

        query.setParameter("uporabnikUsername", username);

        List<UporabnikEntity> resultList = query.getResultList();

        System.out.println("resultList: ");
        for(UporabnikEntity up : resultList) {
            System.out.println(up.getId());
            System.out.println(up.getIme());
            System.out.println(up.getPriimek());
            System.out.println(up.getUsername());
            System.out.println(up.getEmail());
        }

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
