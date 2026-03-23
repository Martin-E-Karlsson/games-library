@param org.example.gameslibrary.dto.CreateGameDto game
@param org.springframework.validation.BindingResult bindingResult = null

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add New Game</title>
</head>
<body>
<h1>Add New Game</h1>
<a href="/games">Back to list</a>

<form action="/games/create" method="post">
    <div>
        <label for="title">Title:</label>
        <input type="text" id="title" name="title" value="${game.getTitle() != null ? game.getTitle() : ""}">
        @if(bindingResult != null && bindingResult.hasFieldErrors("title"))
            <span style="color:red;">${bindingResult.getFieldError("title").getDefaultMessage()}</span>
        @endif
    </div>

    <div>
        <label for="description">Description:</label>
        <textarea id="description" name="description">${game.getDescription() != null ? game.getDescription() : ""}</textarea>
        @if(bindingResult != null && bindingResult.hasFieldErrors("description"))
            <span style="color:red;">${bindingResult.getFieldError("description").getDefaultMessage()}</span>
        @endif
    </div>

    <div>
        <label for="releaseDate">Release Date:</label>
        <input type="date" id="releaseDate" name="releaseDate" value="${game.getReleaseDate() != null ? game.getReleaseDate().toString() : ""}">
        @if(bindingResult != null && bindingResult.hasFieldErrors("releaseDate"))
            <span style="color:red;">${bindingResult.getFieldError("releaseDate").getDefaultMessage()}</span>
        @endif
    </div>

    <div>
        <label for="price">Price:</label>
        <input type="number" id="price" name="price" step="0.01" value="${game.getPrice() != null ? game.getPrice().toString() : ""}">
        @if(bindingResult != null && bindingResult.hasFieldErrors("price"))
            <span style="color:red;">${bindingResult.getFieldError("price").getDefaultMessage()}</span>
        @endif
    </div>

    <div>
        <label>Categories:</label>
        <div id="categories-container">
            @if(game.getCategories() != null && !game.getCategories().isEmpty())
                @for(int i = 0; i < game.getCategories().size(); i++)
                    <div class="category-row">
                        <input type="text" name="categories[${i}]" value="${game.getCategories().get(i)}">
                        <button type="button" onclick="removeCategory(this)">-</button>
                    </div>
                @endfor
            @else
                <div class="category-row">
                    <input type="text" name="categories[0]">
                    <button type="button" onclick="removeCategory(this)">-</button>
                </div>
            @endif
        </div>
        <button type="button" onclick="addCategory()">+ Add Category</button>
    </div>

    <button type="submit">Create Game</button>
</form>

<script>
    function addCategory() {
        const container = document.getElementById('categories-container');
        const rows = container.querySelectorAll('.category-row');
        const index = rows.length;
        const row = document.createElement('div');
        row.className = 'category-row';
        row.innerHTML = '<input type="text" name="categories[' + index + ']">'
            + '<button type="button" onclick="removeCategory(this)">-</button>';
        container.appendChild(row);
    }

    function removeCategory(button) {
        const container = document.getElementById('categories-container');
        const rows = container.querySelectorAll('.category-row');
        if (rows.length > 1) {
            button.parentElement.remove();
            reindexCategories();
        }
    }

    function reindexCategories() {
        const rows = document.querySelectorAll('#categories-container .category-row input');
        rows.forEach(function(input, index) {
            input.name = 'categories[' + index + ']';
        });
    }
</script>
</body>
</html>