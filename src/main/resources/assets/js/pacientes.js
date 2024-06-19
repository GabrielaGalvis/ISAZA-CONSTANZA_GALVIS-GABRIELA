(function() {
    document.querySelector(".toggle-sidebar-btn").addEventListener("click", function(e) {
        document.querySelector('body').classList.toggle('toggle-sidebar')
    });
    cargarDatos();
})();

var simpleDatatable;
function cargarDatos() {
    var datatable = document.querySelector('.datatable')
    fetch('/pacientes/listar')
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

function crearPaciente() {
    var formulario = document.querySelector("#formulario");
    var data = Object.fromEntries(new FormData(formulario));
    data.domicilioEntradaDto = {
        calle: data.calle,
        numero: data.numero,
        localidad: data.localidad,
        provincia: data.provincia,
    };
    fetch("/pacientes/registrar", {
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
       headings: ["ID", "DNI", "Nombre", "Domicilio", "Fecha Ingreso"],
       data: datos.map(item => ([
           item.id,
           item.dni,
           item.nombre + " " + item.apellido,
           item.domicilioSalidaDto.calle + " " + item.domicilioSalidaDto.numero + ", " + item.domicilioSalidaDto.localidad + ", " + item.domicilioSalidaDto.provincia,
           item.fechaIngreso + ""
       ]))
   };
}
