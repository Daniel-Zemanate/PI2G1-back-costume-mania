package com.costumemania.msfavorite.api.service;

import com.costumemania.msfavorite.DTO.FavDTO;
import com.costumemania.msfavorite.DTO.FavModelDTO;
import com.costumemania.msfavorite.DTO.ModelDTO;
import com.costumemania.msfavorite.DTO.ModelFavDTO;
import com.costumemania.msfavorite.client.IProductClient;
import com.costumemania.msfavorite.model.Fav;
import com.costumemania.msfavorite.model.Model;
import com.costumemania.msfavorite.repository.IFavRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import com.costumemania.msfavorite.Util.Query.Query;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import jakarta.persistence.EntityManager;
import java.util.*;

import static com.costumemania.msfavorite.Util.Query.Query.query1;
import static org.springframework.data.domain.Sort.*;

@Service
public class FavService {

    @Autowired
    IFavRepository favRepository;

    @Autowired
    IProductClient iProductClient;


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


    /*
    public List<Object> getByUser(Integer user, Integer limit) {

        List<Object> modelByUserLimit = new ArrayList<>();
        modelByUserLimit =  getByUser(user);
        if(modelByUserLimit.size() >= limit){
            return modelByUserLimit.subList(0,limit);
        }
        else
           return modelByUserLimit.subList(0,modelByUserLimit.size());

    }
    */


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



}
