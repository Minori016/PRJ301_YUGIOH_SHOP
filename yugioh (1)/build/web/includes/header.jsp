<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${pageTitle != null ? pageTitle : "Yu-Gi-Oh! Card Shop"}</title>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet">
    <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet" type="text/css"/>
</head>
<body>
    <!-- =================== NAVBAR =================== -->
    <nav class="navbar navbar-expand-md navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="MainController?txtAction=ourCollection">Our Collection!</a>
            <button class="navbar-toggler" type="button" data-toggle="collapse"
                    data-target="#navbarsExampleDefault" aria-controls="navbarsExampleDefault"
                    aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse justify-content-end" id="navbarsExampleDefault">
                <ul class="navbar-nav m-auto">
                    <li class="nav-item"><a class="nav-link" href="#">Manager Your Account</a></li>
                    <li class="nav-item"><a class="nav-link" href="#">Hello ${user.fullName}</a></li>
                    <li class="nav-item"><a class="nav-link" href="MainController?txtAction=logout">Logout</a></li>
                    <li class="nav-item"><a class="nav-link" href="#">Login</a></li>
                </ul>

                <form action="search" method="post" class="form-inline my-2 my-lg-0">
                    <div class="input-group input-group-sm">
                        <input name="txt" type="text" class="form-control" placeholder="Search...">
                        <div class="input-group-append">
                            <button type="submit" class="btn btn-secondary btn-number">
                                <i class="fa fa-search"></i>
                            </button>
                        </div>
                    </div>
                    <a class="btn btn-success btn-sm ml-3" href="show">
                        <i class="fa fa-shopping-cart"></i> Cart
                        <span class="badge badge-light">3</span>
                    </a>
                </form>
            </div>
        </div>
    </nav>

    <!-- =================== JUMBOTRON =================== -->
    <section class="jumbotron text-center text-white"
             style="background-image: url('https://www.yugioh-card.com/eu/wp-content/uploads/2024/08/LEDD_Reprint_News_Banner.webp');
                    background-size: cover; background-position: center; padding: 120px 0;">
        <div class="container">
            <h1 class="jumbotron-heading fw-bold">Trading Card Game Shop</h1>
            <p class="lead mb-0">Reputation builds the brand ? with over 10 years of providing TCG products such as Yu-Gi-Oh! trading cards.</p>
        </div>
    </section>
