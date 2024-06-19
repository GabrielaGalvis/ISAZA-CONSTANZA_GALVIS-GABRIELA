(function() {
    document.querySelector(".toggle-sidebar-btn").addEventListener("click", function(e) {
        document.querySelector('body').classList.toggle('toggle-sidebar')
    });
    cargarDatos();
})();

var simpleDatatable;
function cargarDatos() {
    var datatable = document.querySelector('.datatable')
    fetch('/odontologos/listar')
        .then(r => r.json())
        .then(datos => {
            var data = crearData(datos);
            simpleDatatable = new simpleDatatables.DataTable(datatable, {
              perPageSelect: [5, 10, 15, ["All", -1]],
              columns: [{
                  select: 2,
                  sortSequence: ["desc", "asc"]
                },
                {
                  select: 3,
                  sortSequence: ["desc"]
                },
                {
                  select: 4,
                  cellClass: "green",
                  headerClass: "red"
                }
              ],
              data: data
            });
        });
}

function crearOdontologo() {
    var formulario = document.querySelector("#formulario");
    var data = Object.fromEntries(new FormData(formulario));
    fetch("/odontologos/registrar", {
        method: "POST",
        headers: {'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    })
    .then(respuesta => respuesta.status === 201 ? respuesta.json() : Promise.resolve(null))
    .then(item => {
        if (item) {
            formulario.reset();
            document.querySelector("#cerrarModalBoton").click();
            simpleDatatable.insert(crearData([item]));
        }
    })
}

function crearData(datos) {
    return {
       headings: ["ID", "Matricula", "Nombre"],
       data: datos.map(item => ([
           item.id,
           item.numeroDeMatricula,
           item.nombre + " " + item.apellido
       ]))
   };
}
