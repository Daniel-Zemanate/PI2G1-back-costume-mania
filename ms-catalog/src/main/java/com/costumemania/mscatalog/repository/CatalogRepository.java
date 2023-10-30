package com.costumemania.mscatalog.repository;

import com.costumemania.mscatalog.model.Catalog;
import com.costumemania.mscatalog.model.CatalogDTO;
import com.costumemania.mscatalog.model.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog, Integer> {

    List<Catalog> findAll ();
    Optional<Catalog> findById (Integer id);
    List<Catalog> findBySize (Size size);



    Catalog create (CatalogDTO catalogDTO);
    void delete(Long id);

    // para informar los ultimos elementos agregados al catalogo, pero no informa las reposiciones de stock
    List<Catalog> findNews ();

    // servirá para actualizar elemento. también para bajar la cantidad disponible de unidades de catalogo luego de la venta
    Catalog update (CatalogDTO catalogDTO);

    // este método requiere de la existencia de una API de Model que me confirme que el numero de modelo que estoy buscando existe
    List<Catalog> findAllByModel (Long idModel);

    // este método requiere de la existencia de una API de Model que me devuelva el número de ID que puedo consultar
    List<Catalog> findAllByModelName (String modelName);

    // este método requiere de la existencia de una API de Category que me confirme que el numero de categoria que estoy buscando existe
    // y requiere una API de Model me devuelva el número de ID que puedo consultar
    List<Catalog> findAllByCategory (Long idCategory);

    // para los métodos de filtros multiples (categoria + palabra clave), puedo usar una combinación de estos métodos simples o llamar a la API de modelo que me devuelva los ID de modelo con las comparaciones ya hechas.
    // para los métodos de filtros multiples (talle + palabra clave, categoria + talle + palabra clave, talle + categoria), usar una combinación de estos métodos simples.
}
