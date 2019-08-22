package lt.bit.java2.servlets;


import lt.bit.java2.EntityManagerHelper;
import lt.bit.java2.entities.Employee;

import javax.persistence.EntityManager;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@WebServlet("/api")
public class ApiServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("utf-8");

        EntityManager em = EntityManagerHelper.getEntityManager();

//        Employee employee = em.find(Employee.class, 10001);
//        resp.getWriter().print(employee.getFirstName() + " " + employee.getLastName());

        List<Employee> employees = getEmployeesByNames(em,"%man%");
        for(Employee employee : employees){
            resp.getWriter().println(employee.getEmpNo() + " " + employee.getFirstName() + " " + employee.getLastName());
        }
        em.close();
        resp.getWriter().print("Hello World");
    }

    private List<Employee> getEmployeesByNames(EntityManager em, String name){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);

        Predicate predicateForName = cb.like(root.get("firstName"),name);

        query.where(predicateForName);

        return em.createQuery(query).setMaxResults(10).getResultList();
    }
}
