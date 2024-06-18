/**
* Template Name: NiceAdmin
* Template URL: https://bootstrapmade.com/nice-admin-bootstrap-admin-html-template/
* Updated: Apr 20 2024 with Bootstrap v5.3.3
* Author: BootstrapMade.com
* License: https://bootstrapmade.com/license/
*/

(function() {
  "use strict";

  /**
   * Easy selector helper function
   */
  const select = (el, all = false) => {
    el = el.trim()
    if (all) {
      return [...document.querySelectorAll(el)]
    } else {
      return document.querySelector(el)
    }
  }

  /**
   * Easy event listener function
   */
  const on = (type, el, listener, all = false) => {
    if (all) {
      select(el, all).forEach(e => e.addEventListener(type, listener))
    } else {
      select(el, all).addEventListener(type, listener)
    }
  }

  /**
   * Sidebar toggle
   */
  if (select('.toggle-sidebar-btn')) {
    on('click', '.toggle-sidebar-btn', function(e) {
      select('body').classList.toggle('toggle-sidebar')
    })
  }

    /**
    * Initiate Datatables
    */
    var datatable = select('.datatable')

    fetch('/pacientes/listar')
        .then(r => r.json())
        .then(datos => {
            var data = {
                headings: ["ID", "DNI", "Nombre", "Domicilio", "Fecha Ingreso"],
                data: datos.map(item => ([
                    item.id,
                    item.dni,
                    item.nombre + " " + item.apellido,
                    item.domicilioSalidaDto.calle + " " + item.domicilioSalidaDto.numero + ", " + item.domicilioSalidaDto.localidad + ", " + item.domicilioSalidaDto.provincia,
                    item.fechaIngreso + ""
                ]))
            }


            new simpleDatatables.DataTable(datatable, {
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
})();