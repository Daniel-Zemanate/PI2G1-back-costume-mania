package com.costumemania.msfavorite.api.service;

import com.costumemania.msfavorite.DTO.*;
import com.costumemania.msfavorite.DTO.CatalogResponse;
import com.costumemania.msfavorite.client.ICatalogClient;
import com.costumemania.msfavorite.client.IProductClient;
import com.costumemania.msfavorite.client.IUserClient;
import com.costumemania.msfavorite.model.*;
import com.costumemania.msfavorite.repository.IFavRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import jakarta.persistence.EntityManager;
import java.util.*;

import static com.costumemania.msfavorite.Util.Query.Query.query1;

@Service
public class FavService {

    @Autowired
    IFavRepository favRepository;

    @Autowired
    IProductClient iProductClient;

    @Autowired
    IUserClient iUserClient;

    @Autowired
    ICatalogClient iCatalogClient;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ObjectMapper mapper;

    public List<Object> getFav(){

        List<Fav> f = favRepository.findAll();
        List<Model> m = iProductClient.getAllModel();
        List<Object> AllFavs = new ArrayList<>();


        for(int i = 0; i<m.size();i++) {

            int x = m.get(i).idModel();

            for (int y = 0; y < f.size(); y++) {

                if (f.get(y).getModel().equals(x)) {

                    ModelFavDTO sf = mapper.convertValue(m.get(i), ModelFavDTO.class);
                    sf.setIdFav(f.get(y).getFavId());
                    sf.setUsers(f.get(y).getUsers());

                    AllFavs.add(sf);
                    System.out.println();

                }
            }
        }

        return AllFavs;
    }
    public Optional<Fav> getFavById(Integer id){
        return favRepository.findById(id);
    }
    public Fav save(Fav fav){return favRepository.save(fav);}
    public void DeleteById (Integer id) {
        favRepository.deleteById(id);
    }

    public List<Object> getByUser(Integer user) {
        List<Integer> e = favRepository.findByUser(user);
        List<Fav> f = favRepository.findFavByUser(user);
        List<Model> m = iProductClient.getAllModel();

        List<Object> modelByUser = new ArrayList<>();


        for(int i = 0; i<m.size();i++){

            int x = m.get(i).idModel();

            for (int y = 0; y<e.size(); y++){

                if(f.get(y).getModel().equals(x)){

                    ModelDTO sf = mapper.convertValue(m.get(i), ModelDTO.class);
                    sf.setIdFav(f.get(y).getFavId());

                    modelByUser.add(sf);
                    System.out.println();

            }
            }
        }

        return modelByUser;
    }



    public List<FavModelDTO> FavOrderModel() {

        List<Fav> f = favRepository.findAll();
        List<FavDTO> favDTOS = new ArrayList<>();
        List<Model> m = iProductClient.getAllModel();
        var query = this.entityManager.createNativeQuery(query1, Integer.class);
        List<Integer> Favs = query.getResultList();
        List<FavModelDTO> FinalDto = new ArrayList<>();
        int limit = 8;

        for (int x = 0; x<Favs.size(); x++){

            int count = 0;

            for (int y = 0; y<f.size(); y++){

                if(Favs.get(x).equals(f.get(y).getModel())){
                    count += 1;
                }
            }

            FavDTO fr = new FavDTO(Favs.get(x),count);
            favDTOS.add(fr);

        }

        for (int z=0;z<favDTOS.size();z++){

            if(favDTOS.get(z).getCount()>1){

                for (int y=0; y<m.size();y++){

                    if(favDTOS.get(z).getModel().equals(m.get(y).idModel())){

                        FavModelDTO fn = mapper.convertValue(m.get(y), FavModelDTO.class);
                        fn.setCount(favDTOS.get(z).getCount());
                        FinalDto.add(fn);
                    }
                }
            }
        }

        FinalDto.sort(Comparator.comparing(FavModelDTO::getCount).reversed());

        if(FinalDto.size()>limit){
            return FinalDto.subList(0,limit);
        }
        return FinalDto;


        }


    public List<CatalogResponse> FavOrderModelV2() {

        List<Fav> f = favRepository.findAll();  // busco todos los favoritos
        List<FavDTO> favDTOS = new ArrayList<>();   // creo una lista de fav DTOS para hacer el conteo de cuantos tiene mas like
        List<Model> m = iProductClient.getAllModel(); // creo una lista de todos los modelos que tengo en ms product
        var query = this.entityManager.createNativeQuery(query1, Integer.class);
        List<Integer> Favs = query.getResultList();   // obtengo el conteo de la query
        List<CatalogDTO> cts = iCatalogClient.getAll();
        List<CatalogResponse> FinalDto = new ArrayList<>();
        int limit = 8;

        for (int x = 0; x<Favs.size(); x++){

            int count = 0;

            for (int y = 0; y<f.size(); y++){

                if(Favs.get(x).equals(f.get(y).getModel())){
                    count += 1;
                }
            }

            FavDTO fr = new FavDTO(Favs.get(x),count);
            favDTOS.add(fr);

        }

        for (int z=0;z<favDTOS.size();z++){

            if(favDTOS.get(z).getCount()>1){

                for (int y=0; y<m.size();y++){

                    if(favDTOS.get(z).getModel().equals(m.get(y).idModel())){

                        CatalogResponse fn = mapper.convertValue(m.get(y), CatalogResponse.class);
                        fn.setCount(favDTOS.get(z).getCount());
                        Float price = 0.0f;
                        List<CatalogResponse.SizeByModel> listSize = new ArrayList<>();
                        for(int p = 0; p<cts.size(); p++){
                            if(cts.get(p).getModel().getIdModel().equals(m.get(y).idModel())){
                                price  = cts.get(p).getPrice();

                                listSize.add(new CatalogResponse.SizeByModel(cts.get(p).getIdCatalog(),
                                        cts.get(p).getSize().getNoSize(),
                                        cts.get(p).getStock()));

                            }

                        }

                        fn.setPrice(price);
                        fn.setSizeType(cts.get(z).getSize().getAdult());
                        fn.setSizes(listSize);
                        FinalDto.add(fn);
                    }
                }
            }
        }

        FinalDto.sort(Comparator.comparing(CatalogResponse::getCount).reversed());

        if(FinalDto.size()>limit){
            return FinalDto.subList(0,limit);
        }
        return FinalDto;


    }


        public Integer findUser(Integer us) {

            try {
                List<UsersEntity> users = iUserClient.allUsers();
                Integer find = 0;

                for (int x = 0; x < users.size(); x++) {
                    Integer z = Integer.valueOf(users.get(x).id());
                    if (z == us) {
                        find = 1;
                    }
                }
                return find;


            } catch (Exception e) {
                return 99;
            }
        }


        public Integer findModel (Integer mod){

        try {


            List<Model> m = iProductClient.getAllModel();
            Integer model = 0;
            for (int x = 0; x < m.size(); x++) {
                Integer z = Integer.valueOf(m.get(x).idModel());
                if (z == mod) {
                    model = 1;
                }

            }
            return model;

        }
        catch (Exception e) {
            return 99;
        }


        }

        public Integer findCatalog (Integer cat){

        try {


            List<CatalogDTO> m = iCatalogClient.getAll();
            Integer catl = 0;
            for (int x = 0; x < m.size(); x++) {
                Integer z = Integer.valueOf(m.get(x).getIdCatalog());
                if (z == cat) {
                    catl = 1;
                }

            }
            return catl;

        }
        catch (Exception e) {
            return 99;
        }


    }



}
