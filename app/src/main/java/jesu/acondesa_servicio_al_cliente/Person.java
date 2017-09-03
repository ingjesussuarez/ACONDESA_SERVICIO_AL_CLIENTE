package jesu.acondesa_servicio_al_cliente;

class Person {
    String nombre;
    String direccion;
    String telefono;
    int id;


    Person(){
        nombre = null;
        direccion = null;
        telefono = null;
        id = R.mipmap.carrito_compras;
    }

    Person(String nomb, String dir, int id, String tel) {
        this.nombre = nomb;
        this.direccion = dir;
        this.telefono = tel;
        this.id = id;
    }

}