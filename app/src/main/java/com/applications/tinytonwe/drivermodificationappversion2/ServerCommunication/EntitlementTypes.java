package com.applications.tinytonwe.drivermodificationappversion2.ServerCommunication;

/**
 * Created by admin on 3/12/2015.
 * This is an enum class representing the different Entitlement types a user can use
 */
public enum EntitlementTypes
{
    GENERAL((short)1),
    FOOD((short)2) ,
    TRAFFICTRACK((short)3),
    TINYTRACK((short)4),
    TRAIN((short)5),
    ARCADE((short)6),
    MEMBERADVANTAGES((short)7),
    TRANSFERCREDITS((short)8);

    private short value;

    private EntitlementTypes(short value){
        this.value = value;
    }

    public short getEnumValue(){
        return this.value;
    }
}
