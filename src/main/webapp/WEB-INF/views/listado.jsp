<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>MiniInventario - Productos</title>
  <style>
    body{font-family: system-ui, sans-serif; max-width: 1000px; margin: 2rem auto;}
    table{width:100%; border-collapse: collapse;}
    th,td{border:1px solid #ddd; padding: .5rem; text-align:left;}
    .actions form{display:inline-block; margin-right:.25rem;}
    .ok{background:#e6ffed; border:1px solid #a3f7b5; padding:.5rem; margin:.5rem 0;}
    .err{background:#ffecec; border:1px solid #ffb3b3; padding:.5rem; margin:.5rem 0;}
    .toolbar{display:flex; justify-content:space-between; align-items:center; margin-bottom:1rem;}
    form.inline{display:flex; gap:.5rem; align-items:center;}
    button.linklike{background:none; border:none; padding:0; text-decoration:underline; cursor:pointer;}
  </style>
</head>
<body>

  <div class="toolbar">
    <h1>Inventario</h1>
    <a href="${pageContext.request.contextPath}/productos?action=create">Nuevo producto</a>
  </div>

  <c:if test="${not empty flash}">
    <c:choose>
      <c:when test="${flash.type == 'ok' || flash.type == 'success'}"><div class="ok">${flash.message}</div></c:when>
      <c:otherwise><div class="err">${flash.message}</div></c:otherwise>
    </c:choose>
  </c:if>

  <form class="inline" method="get" action="${pageContext.request.contextPath}/productos">
    <label>Categoría</label>
    <select name="categoria">
      <option value="">(todas)</option>
      <c:forEach var="categ" items="${categorias}">
        <option value="${categ}" <c:if test="${categ == categoriaActual}">selected</c:if>>${categ}</option>
      </c:forEach>
    </select>

    <label>
      <input type="checkbox" name="soloAgotados" <c:if test="${soloAgotados}">checked</c:if>/>
      Solo agotados
    </label>
    <button type="submit">Filtrar</button>
  </form>

  <table>
    <thead>
    <tr>
      <th>ID</th><th>Nombre</th><th>Categoría</th><th>Precio (€)</th><th>Stock</th><th>Agotado</th><th>Acciones</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="p" items="${productos}">
      <tr>
        <td>${p.id}</td>
        <td>${p.nombre}</td>
        <td>${p.categoria}</td>
        <td>${p.precio}</td>
        <td>${p.stock}</td>
        <td><c:out value="${p.agotado ? 'Sí' : 'No'}"/></td>
        <td class="actions">

          <!-- +1 -->
          <form method="post" action="${pageContext.request.contextPath}/productos">
            <input type="hidden" name="action" value="inc"/>
            <input type="hidden" name="id" value="${p.id}"/>
            <input type="hidden" name="categoria" value="${categoriaActual}"/>
            <c:if test="${soloAgotados}">
              <input type="hidden" name="soloAgotados" value="on"/>
            </c:if>
            <button type="submit" class="linklike">+1</button>
          </form>

          <!-- -1 -->
          <form method="post" action="${pageContext.request.contextPath}/productos">
            <input type="hidden" name="action" value="dec"/>
            <input type="hidden" name="id" value="${p.id}"/>
            <input type="hidden" name="categoria" value="${categoriaActual}"/>
            <c:if test="${soloAgotados}">
              <input type="hidden" name="soloAgotados" value="on"/>
            </c:if>
            <button type="submit" class="linklike">−1</button>
          </form>

          <!-- Toggle agotado -->
          <form method="post" action="${pageContext.request.contextPath}/productos">
            <input type="hidden" name="action" value="toggle"/>
            <input type="hidden" name="id" value="${p.id}"/>
            <input type="hidden" name="categoria" value="${categoriaActual}"/>
            <c:if test="${soloAgotados}">
              <input type="hidden" name="soloAgotados" value="on"/>
            </c:if>
            <button type="submit" class="linklike">Toggle agotado</button>
          </form>

          <!-- Delete (solo stock=0) -->
          <c:choose>
            <c:when test="${p.stock == 0}">
              <form method="post" action="${pageContext.request.contextPath}/productos"
                    onsubmit="return confirm('¿Eliminar producto?');">
                <input type="hidden" name="action" value="delete"/>
                <input type="hidden" name="id" value="${p.id}"/>
                <input type="hidden" name="categoria" value="${categoriaActual}"/>
                <c:if test="${soloAgotados}">
                  <input type="hidden" name="soloAgotados" value="on"/>
                </c:if>
                <button type="submit" class="linklike">Eliminar</button>
              </form>
            </c:when>
            <c:otherwise>
              <span style="opacity:.5" title="Solo con stock 0">Eliminar</span>
            </c:otherwise>
          </c:choose>

        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>

</body>
</html>
