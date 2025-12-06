# GestorRestaurante_JAVA
Aplicacion Java Swing con patron Decorator

Descripcion
ESte proyecto lo realice en java usando swing para ofrecer una interfaz grafica que permite gestionar pedidos de un restaurante.

Este sistema es desarrollado para que se pueda
* Crear Pedidos
* Seleccionar platillos del menu
* Agregar ingredientes extras acorde al tipo de platillo solicitado
* Mostrar lista de pedidos con estados diferenciados usando tonos para diferenciar
* Ver detalles completos de un pedido
* Actualizar estados del pedido(En preparacion, listo, enviado, entregado, cancelado).
* Eliminar pedidos cancelados.
* Mostrar pedidos correctamentes formateados.

Esos son las opciones que puede realizar mi aplicacion, usando de enfoque principal el uso de java swing y implementando el patron de disenio Decorator para permitir que los platillos puedan extenderse con extras sin modificar su estructura base.

#Herramientas usadas
* Java 21jdk
* Swing
* Patron de disenio Decorator
* IDE Netbeans

#Interfaz grafica
implemente varios paneles de swing usando:
* JPanel
* JButton
* JComboBox
* JCheckBox
* JList
* JTable
* DefaultTableModel
* FlowLayout
* BorderLayout
* GridBagLayout

#Por que el uso del patron de disenio Decorator?
Porque Decorator permite muchas funciones necesarias en mi aplicacion como>
* Agregar extras dinamicamente.
* Calcular precios sumando el costo de cada extra.
* Mantener el codigo limpio y extensible.
* Evitar multiples subclases inncesarias, ejemplo(hamburgues con queso, hamburguesa con bacon).



