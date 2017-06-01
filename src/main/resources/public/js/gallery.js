/**
 * Created by flowerpower on 2017. 05. 15..
 */
function GalleryLoader(houseId) {
    this.houseId = houseId;
    this.currentPhoto = 0;
    this.photos;

    this.getPhotos = function() {
        var self = this;
        $.ajax({
            "method": "GET",
            "url": "/api/get-pictures/"+this.houseId,
            "contentType": "application/javascript;charset=UTF8",
            "dataType": "json",
            "success": function(json) {
                self.photos = json;
                self.loadCurrent();
            }

        });
    };

    this.loadCurrent = function() {
        var self = this;
        var picModalTitle = $("#picmodal-title");
        var picModalImg = $("#picmodal-img");

        picModalTitle.html(self.photos[self.currentPhoto].description);
        picModalImg.attr("alt", self.photos[self.currentPhoto].description);
        picModalImg.attr("src", self.photos[self.currentPhoto].url);

    };
}

function ShareholdSeller(houseId) {
    this.houseId = houseId;

    this.loadCurrent = function() {
        var shareholds = $("#shareholds"+houseId).val();
        var boughtPrice = $("#boughtPrice"+houseId).val();
        console.log(shareholds);

        $("#max-quantity-span").html(shareholds);
        $("#quantity").attr("max", shareholds);
        $("#market-form").attr("action", "/market/sell/"+houseId);
        $("#id").val(houseId);
        $("#boughtPrice").val(boughtPrice);
    }
}

$(document).ready(function() {
    var galleryLinks = $(".galleryLink");
    var sellButtons = $(".sell");

    galleryLinks.click(function() {
        var btn = $(this);
        var galleryLoader = new GalleryLoader(getHouseId(btn));
        galleryLoader.getPhotos();
    });

    sellButtons.click(function() {
        var btn = $(this);
        var shareholdSeller = new ShareholdSeller(getHouseId(btn));
        shareholdSeller.loadCurrent();
    });

    function getHouseId(element) {
        var houseIdWithPrefix = element.parent().parent().attr('id');
        return houseIdWithPrefix.substring(5, houseIdWithPrefix.length);
    }
});