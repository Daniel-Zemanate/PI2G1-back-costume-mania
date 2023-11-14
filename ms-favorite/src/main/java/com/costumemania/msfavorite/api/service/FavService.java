package com.costumemania.msfavorite.api.service;

import com.costumemania.msfavorite.DTO.ModelDTO;
import com.costumemania.msfavorite.client.IProductClient;
import com.costumemania.msfavorite.model.Fav;
import com.costumemania.msfavorite.model.Model;
import com.costumemania.msfavorite.repository.IFavRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FavService {

    @Autowired
    IFavRepository favRepository;

    @Autowired
    IProductClient iProductClient;

    @Autowired
    private ObjectMapper mapper;

    public List<Fav> getFav(){
        return favRepository.findAll();
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
}
