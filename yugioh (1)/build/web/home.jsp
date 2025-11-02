<%@ include file="includes/header.jsp" %>

<!-- =================== MAIN CONTENT =================== -->
<div class="container">
    <div class="row">
        <div class="col-sm-3">
            <!-- Sidebar -->
            <div class="card bg-light mb-3">
                <div class="card-header bg-primary text-white text-uppercase">
                    <i class="fa fa-list"></i> Categories
                </div>
                <ul class="list-group category_block">
                    <c:forEach items="${listCater}" var="o">
                        <li class="list-group-item text-white">
                            <a href="MainController?txtAction=categoryProduct&setID=${o.setID}">${o.setName}</a>
                        </li>
                    </c:forEach>
                </ul>
            </div>

            <c:if test="${not empty p}">
                <div class="card bg-light mb-3 shadow-sm border-0">
                    <div class="card-header bg-success text-white text-uppercase text-center">
                        <i class="fa fa-star"></i> Staff's Pick
                    </div>
                    <div class="card-body text-center">
                        <div class="d-flex justify-content-center align-items-center bg-white p-2"
                             style="height:240px; overflow:hidden; border-radius:8px;">
                            <img src="${p.image}" alt="${p.cardName}"
                                 style="width:100%; height:auto; max-height:230px; object-fit:scale-down;">
                        </div>
                        <h5 class="card-title font-weight-bold mb-1 mt-2">${p.cardName}</h5>
                        <p class="card-text text-muted mb-2">${p.rarity}</p>
                        <p class="h5 text-danger mb-0">${p.price} $</p>
                    </div>
                </div>
            </c:if>
        </div>

        <div class="col-sm-9">
            <div class="row">
                <c:forEach items="${listC}" var="o">
                    <div class="col-12 col-md-6 col-lg-4">
                        <div class="card">
                            <img class="card-img-top" src="${o.image}" alt="Card image cap">
                            <div class="card-body">
                                <h4 class="card-title show_txt">
                                    <a href="MainController?txtAction=viewDetail&cardID=${o.cardID}" title="View Product">${o.cardName}</a>
                                </h4>
                                <p class="card-text show_txt">${o.rarity}</p>
                                <div class="row">
                                    <div class="col">
                                        <p class="btn btn-danger btn-block">${o.price} $</p>
                                    </div>
                                    <div class="col">
                                        <a href="#" class="btn btn-success btn-block">Add to cart</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>

<%@ include file="includes/footer.jsp" %>
