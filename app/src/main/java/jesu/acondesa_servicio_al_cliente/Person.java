package jesu.acondesa_servicio_al_cliente;

class Person {
    String nombre;
    String direccion;
    int id;


    Person(){
        nombre = null;
        direccion = null;
        id = R.mipmap.carrito_compras;
    }

    Person(String nomb, String dir, int id) {
        this.nombre = nomb;
        this.direccion = dir;
        this.id = id;
    }

}