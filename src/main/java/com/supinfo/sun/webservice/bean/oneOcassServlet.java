package com.supinfo.sun.webservice.bean;

import com.supinfo.sun.webservice.mapping.Objects;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;


@WebServlet(name = "OneoccasServlet", value = "/api/objects")
public class oneOcassServlet extends HttpServlet {
    EntityManagerFactory entityManagerFactory = null;
    EntityManager entityManager = null;




    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        entityManagerFactory = Persistence.createEntityManagerFactory("webservices");
        entityManager = entityManagerFactory.createEntityManager();
        PrintWriter out = response.getWriter();
        ObjectJsonConverter objectJsonConverter = new ObjectJsonConverter();
        Map params = request.getParameterMap();
        if(params.size()<1){
            List<Objects> objects = entityManager.createQuery("FROM Objects ",Objects.class).getResultList();
            for(Objects object: objects){
                out.println(objectJsonConverter.convertToDatabaseColumn(object));
            }
        }
        if (params.containsKey("titre")){
            List<Objects> objectsTitre = entityManager.createQuery("FROM Objects WHERE titre LIKE '%"+ request.getParameter("titre")+"%' ",Objects.class).getResultList();
            for(Objects objectT: objectsTitre) {
                out.println(objectJsonConverter.convertToDatabaseColumn(objectT));
            }
        }
        if (params.containsKey("description")){
            List<Objects> objectsDesc = entityManager.createQuery("FROM Objects WHERE description LIKE '%"+ request.getParameter("description")+"%' ",Objects.class).getResultList();
            for(Objects objectT: objectsDesc) {
                out.println(objectJsonConverter.convertToDatabaseColumn(objectT));
            }
        }
        if (params.containsKey("type")) {
            List<Objects> objectsType = entityManager.createQuery("FROM Objects WHERE type = '" + request.getParameter("type") + "' ", Objects.class).getResultList();
            for (Objects objectT : objectsType) {
                out.println(objectJsonConverter.convertToDatabaseColumn(objectT));
            }
        }
        if (params.containsKey("price")) {
            List<Objects> objectsP = entityManager.createQuery("FROM Objects WHERE prix = '" + request.getParameter("price") + "' ", Objects.class).getResultList();
            for (Objects objectT : objectsP) {
                out.println(objectJsonConverter.convertToDatabaseColumn(objectT));
            }
        }
        out.flush();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
