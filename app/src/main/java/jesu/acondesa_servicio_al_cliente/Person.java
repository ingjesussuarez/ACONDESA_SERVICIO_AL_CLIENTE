package jesu.acondesa_servicio_al_cliente;

class Person {
    String nombre;
    String direccion;
    String telefono;
    String id_person;
    String data;
    int id_mipmap;


    Person(){
        nombre = null;
        direccion = null;
        telefono = null;
        id_person = null;
        data = null;
        id_mipmap = 0;
    }

    Person(String nomb, String dir, int id_mipmap, String tel,String id_person,String data) {
        this.nombre = nomb;
        this.direccion = dir;
        this.telefono = tel;
        this.id_person = id_person;
        this.data = data;
        this.id_mipmap = id_mipmap;

    }

}