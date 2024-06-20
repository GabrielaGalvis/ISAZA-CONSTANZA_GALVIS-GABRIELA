(function() {
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
    .then(respuesta => {
        document.querySelectorAll(".validation-error").forEach(item => item.innerText = '');
        if (respuesta.status !== 201) {
            respuesta.json().then(mensajes => {
                var entries = Object.entries(mensajes);
                entries.forEach(([id, mensaje]) => {
                    document.querySelector("#" + id).innerText = mensaje;
                })
            });
            return Promise.resolve(null);
        }
        return respuesta.json();
    })
    .then(item => {
        if (item) {
            document.querySelectorAll(".validation-error").forEach(item => item.innerText = '');
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
