package net.veilmc.webcore.utils;

public class HashUtils {

    public String getHash(String passwd){
        return BCrypt.hashpw(passwd, "$2a$10$I0J4ddTI9t3ZKZrUJHf7ne");
    }

    public boolean checkEquals(String plaintext, String hashed){
        return BCrypt.checkpw(plaintext, hashed);
    }


}
