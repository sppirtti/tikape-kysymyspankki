package tikape.runko;


import java.util.HashMap;
import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;
import tikape.runko.database.KysymysDao;
import tikape.runko.database.VastausDao;
import tikape.runko.domain.Kysymys;
import tikape.runko.domain.Vastaus;

/**
 *
 * @author Samuli
 */


public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        if (System.getenv("PORT") != null) {
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }

        Database database = new Database("jdbc:sqlite:kysymyspankki.db");
        KysymysDao kysymysDao = new KysymysDao(database);
        VastausDao vastausDao = new VastausDao(database);

        Spark.get("/", (req, res) -> {

            HashMap map = new HashMap<>();
            map.put("kysymykset", kysymysDao.findAll());

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        Spark.post("/luo", (req, res) -> {

            Kysymys kys = new Kysymys(-1, req.queryParams("kurssi"), req.queryParams("aihe"), req.queryParams("kysymysteksti"));

            kysymysDao.save(kys);

            res.redirect("/");

            return "";
        });

        Spark.post("/kysymys/poista/:id", (req, res) -> {

            Integer id = Integer.parseInt(req.params(":id"));
            kysymysDao.delete(id);

            res.redirect("/");

            return "";

        });

        Spark.get("/kysymykset/:id", (req, res) -> {

            HashMap map = new HashMap<>();
            System.out.println("toimii");
            Integer id = Integer.parseInt(req.params(":id"));
            System.out.println("toimii");
            map.put("kysymys", kysymysDao.findOne(id));
            
            map.put("vastaukset", vastausDao.findKysymys(id));
            System.out.println("toimii");
            return new ModelAndView(map, "kysymys");
           
        }, new ThymeleafTemplateEngine());

        Spark.post("/kysymykset/create/:id", (req, res) -> {
            System.out.println("postaus");
            
            Integer id = Integer.parseInt(req.params(":id"));
            System.out.println("saatuid");
            Boolean oik = true;
            if (req.queryParams("oikein") == null) {
                oik = false;
            }
            System.out.println("oikeus");
            
            
            vastausDao.save(new Vastaus(-1, id, req.queryParams("vastausteksti"), oik));
            
            res.redirect("/kysymykset/" + id);
           
            return "";

        });

        Spark.post("/vastaus/poista/:id", (req, res) -> {
            Integer id = Integer.parseInt(req.params(":id"));

            vastausDao.delete(id);
            
            res.redirect("/kysymykset/" + id);
            
            return "";

        });

    }

}
