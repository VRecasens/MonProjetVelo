package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(5, "greendao");

        //Table Vélo
        Entity station = schema.addEntity("Station");

        station.addLongProperty("number").primaryKey();
        station.addStringProperty("name");
        station.addStringProperty("address");
        Property positionId = station.addLongProperty("positionId").notNull().getProperty();
        station.addBooleanProperty("banking");
        station.addBooleanProperty("bonus");
        station.addStringProperty("status");
        station.addStringProperty("contract_name");
        station.addIntProperty("bike_stands");
        station.addIntProperty("available_bike_stands");
        station.addIntProperty("available_bikes");
        station.addLongProperty("last_update");
        station.setHasKeepSections(true);

        Entity position = schema.addEntity("Position");
        position.addIdProperty();
        position.addDoubleProperty("lat");
        position.addDoubleProperty("lng");

        //Relation Station / Position
        station.addToOne(position, positionId);

        new DaoGenerator().generateAll(schema, args[0]);
    }
}
