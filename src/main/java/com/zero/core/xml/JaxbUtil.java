package com.zero.core.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.zero.core.domain.QueryConfig;
import com.zero.core.domain.QueryConfiguration;

public class JaxbUtil {
    public static void main(String[] args) throws Exception {
        //writeQueryConfig();
        //readQueryConfig();
    }

    protected static void writeQueryConfig() {
        //String outPath = JaxbDemo.class.getResource("/").getFile() + "alldatas.xml"; //output to bin directory
        String outPath = "alldatas.xml"; //output to current directory
        List<QueryConfig> wishes = new ArrayList<QueryConfig>();
        wishes.add(new QueryConfig("query1", "Audi", "select * from t1"));
        wishes.add(new QueryConfig("query2", "Benz", "delete t2"));

        try {
            JAXBContext context = JAXBContext.newInstance(QueryConfiguration.class, QueryConfig.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            //Will write property or public field
            QueryConfiguration all = new QueryConfiguration(wishes);

            m.marshal(all, new FileOutputStream(new File(outPath)));
        } catch (Exception jex) {
            System.out.println("JAXB Binding Exception");
            jex.printStackTrace();
        }
    }

    static QueryConfiguration readQueryConfig() throws Exception {
        String fileName = JaxbUtil.class.getResource("/").getFile() + "query.xml"; // src/query.xml
        JAXBContext jc = JAXBContext.newInstance(new Class[] { QueryConfiguration.class, QueryConfig.class });
        Unmarshaller u = jc.createUnmarshaller();
        QueryConfiguration queryConfiguration = (QueryConfiguration) u.unmarshal(new File(fileName));
        
        System.out.println(queryConfiguration.getQueryList());
        return queryConfiguration;
    }

}
