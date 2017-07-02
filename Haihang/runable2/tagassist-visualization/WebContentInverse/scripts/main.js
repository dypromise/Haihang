var T = 120000;
var FlightsPerPage = 8;
var PAGE_INTERVAL = 10000;
var CONNECT_INTERVAL = 5000;
var EMPTY_MESSAGE = '创新思维 智慧海航';
var url = 'ws://localhost:61614/stomp';
var login = 'guest';
var passcode = 'guest';

function buildClock() {
    var now = new Date();
    var date = now.format('yyyy年m月d日');
    var time = now.format('H:MM:ss');
    $('#date').text(date);
    $('#time').text(time);
    setInterval(function () {
        var time = new Date().format('H:MM:ss');
        $('#time').text(time);
    }, 1000);
}

var Move = function(startPosition, offset) {
    var dotPath = $('#dot-track');
    var baggage = $('.baggage');
    offset = (offset - 0.5) * baggage.height();
    var r0 = parseFloat(dotPath.css('border-radius'));      // original radius
    var l = dotPath.width() - 2 * r0;                       // width
    var h = dotPath.height() - 2 * r0;                      // height
    var r = r0 - offset;
    var o = 2 / T * (Math.PI + (l + h) / r0);               // angular speed
    var borderWidth = parseFloat(dotPath.css("border-width"));
    var xoffset = -1 + dotPath.position().left + parseFloat(dotPath.css("margin-left")) + borderWidth - baggage.width() / 2 + offset;
    var yoffset = -1 + dotPath.position().top + parseFloat(dotPath.css("margin-top")) + borderWidth - baggage.height() / 2 + offset;
    var v = r0 * o;                                         // line speed
    var d = Math.PI / (2 * o);
    // tracktunnels
    var d1 = 0.93 * T;
    var d2 = 0.93 * T;
    var d3 = 0.93 * T;
    var d4 = 0.93 * T;
    var d5 = 0.93 * T;
    // intervals
    var t1 = l / v;
    var t2 = t1 + d;
    var t3 = t2 + h / v;
    var t4 = t3 + d;
    var t5 = t4 + l / v;
    var t6 = t5 + d;
    var t7 = t6 + h / v;
    var t8 = t7 + d;
    this.css = function (t) {
        var x, y, angle;
        t = (t + startPosition + 0.5) * T % T;
        if (t < t1) {
            angle = 0;
            x = v * t + r;
            y = 0;
        } else if (t < t2) {
            angle = o * t - l / r0;
            x = l + r * (1 + Math.sin(angle));
            y = r * (1 - Math.cos(angle));
        } else if (t < t3) {
            angle = Math.PI / 2;
            x = l + 2 * r;
            y = v * t + r - l - Math.PI * r0 * 0.5;
        } else if (t < t4) {
            angle = o * t - (l + h) / r0;
            x = l + r * (1 + Math.sin(angle));
            y = h + r * (1 - Math.cos(angle));
        } else if (t < t5) {
            angle = 0;
            x = 2 * l + h + r + Math.PI * r0 - v * t;
            y = 2 * r + h;
        } else if (t < t6) {
            angle = o * t - (2 * l + h) / r0;
            x = r * (1 + Math.sin(angle));
            y = h + r * (1 - Math.cos(angle));
        } else if (t < t7) {
            angle = Math.PI / 2;
            x = 0;
            y = 2 * (l + h) + r + Math.PI * r0 * 1.5 - v * t;
        } else if (t < t8) {
            angle = o * t - 2 * (l + h) / r0;
            x = r * (1 + Math.sin(angle));
            y = r * (1 - Math.cos(angle));
        }
        var rst = {top: yoffset + y + "px", left: xoffset + x + "px", transform: 'rotate(' + angle + 'rad)'};
        if (Math.abs(t-d1) < 10
            || Math.abs(t-d2) < 10
            || Math.abs(t-d3) < 10
            || Math.abs(t-d4) < 10
            || Math.abs(t-d5) < 10)
            rst.off = true;
        return rst;
    }
}

function setUpAnimation(id, start, offset) {
    $(id).stop().animate({path: new Move(start, offset)}, T, 'linear');
    setInterval(function() {
        $(id).stop().animate({path: new Move(start, offset)}, T, 'linear');
    }, T);
}

var Baggage = Backbone.Model.extend({
    defaults: {
        'exists': true
    }
});

var BaggageCollection = Backbone.Collection.extend({

    model: Baggage,

    initialize: function() {
        this.on("add", this.addBaggage);
        this.on("remove", this.removeBaggage);
        this.on("change", this.changeBaggage);
    },

    addBaggage: function(baggage) {
        var code = baggage.get("code");
        var start = baggage.get("start");
        var template = _.template($('#baggage-template').html());
        $("#" + baggage.id).remove();
        $("#baggages").append(template(baggage.toJSON()));
        setUpAnimation("#" + baggage.id, start, Math.random());
    },

    removeBaggage: function(baggage) {
        $("#" + baggage.id).remove();
    },

    changeBaggage: function(baggage) {
        this.removeBaggage(baggage);
        this.addBaggage(baggage);
    }
});

var Flight = Backbone.Model.extend({
    defaults: {
        'baggages': new BaggageCollection()
    }
});

var FlightCollection = Backbone.Collection.extend({

    model: Flight,

    pagination : function(perPage, page) {
        page = page - 1;
        var collection = this;
        collection = _(collection.rest(perPage*page));
        collection = _(collection.first(perPage));    
        return collection.map(function (model) { return model.toJSON(); }); 
    }
});

var FlightItemView = Backbone.View.extend({

    template: _.template($('#flight-item-template').html()),

    render: function() {
       var html = this.template(this.model);
       this.setElement($(html));
       return this;
    }
});

var FlightView = Backbone.View.extend({

    tagName: 'ul',

    initialize: function() {
        this.collection.bind("all", this.render, this);
        this.currentPage = 1;
        $("#board").append(this.render().el);
    },

    nextPage: function() {
        if (++this.currentPage > this.totalPages()) {
            this.currentPage = 1;
        }
        this.collection.trigger("next_page");
    },

    totalPages: function() {
        return parseInt((this.collection.length - 1) / this.options.perPage + 1);
    },

    render: function() {
        this.$el.empty();
        this.$el.append($('#flight-header-template').html());
        var hasData = false;
        var flights = this.collection.pagination(this.options.perPage, this.currentPage);
        _.each(flights,
            function(flight) {
                var pageview = new FlightItemView({ model: flight });
                var $tr = pageview.render().$el;           
                this.$el.append($tr);
                hasData = true;
            },this);
        if (hasData) {
            this.$el.append('<li class="paginator"><span>'
                + this.currentPage + ' / ' + this.totalPages()
                + '</span></li>');
        } else {
            this.$el.append('<li class="empty"><span>暂无数据</span></li>'
                + '<li class="paginator"><span>0 / 0</span></li>');
        }
        return this;
    }
});

var Message = Backbone.Model.extend();

var MessageCollection = Backbone.Collection.extend({
    model: Message,

    initialize: function() {
        this.on("add", this.addMessage);
        this.on("change", this.changeMessage);
        this.timeouts = {};
    },

    addMessage: function(message) {
        var that = this;
        this.timeouts[message.id] = setTimeout(function() {
            that.remove(message);
        }, message.get('expire') - new Date().getTime());
    },

    changeMessage: function(message) {
        clearTimeout(this.timeouts[message.id]);
        this.addMessage(message);
    }
});

function getObject(msg) {
    return JSON.parse(msg.substr(msg.indexOf(',') + 1));
}

function getCode(id) {
    return id.split('/')[0];
}

function getDisplayCode(id) {
    return id.substr(0, id.length - 4);
}

function buildClient() {
    var VBaggageTracked = "com.greenorbs.tagassist.messagebus.message.VisualProxyMessages$VBaggageTracked";
    var VBaggageRemoved = "com.greenorbs.tagassist.messagebus.message.VisualProxyMessages$VBaggageRemoved";

    var VFlightUpdated = "com.greenorbs.tagassist.messagebus.message.VisualProxyMessages$VFlightUpdated";
    var VFlightRemoved = "com.greenorbs.tagassist.messagebus.message.VisualProxyMessages$VFlightRemoved";
    
    var VSyncRequest = "com.greenorbs.tagassist.messagebus.message.VisualProxyMessages$VSyncRequest";

    var VNotificationUpdated = 'com.greenorbs.tagassist.messagebus.message.VisualProxyMessages$VNotificationUpdated';
    var VNotificationRemoved = 'com.greenorbs.tagassist.messagebus.message.VisualProxyMessages$VNotificationRemoved';

    window.flightCollection = new FlightCollection();

    window.flightView = new FlightView({
        collection: flightCollection,
        perPage: FlightsPerPage
    });

    window.messageCollection = new MessageCollection();

    var client = Stomp.client(url);

    var onconnect = function() {
        client.send('/topic/' + VSyncRequest, {}, 'com.greenorbs.tagassist.messagebus.message.VisualProxyMessages$VSyncRequest,{"component":0,"customClass":"","customJSON":"","messageId":"","name":"","needConfirm":false,"source":""}');
        client.subscribe('/topic/' + VBaggageTracked, function(message) {
            var msg = getObject(message.body);
            var flight = flightCollection.get(msg.flightId);
            if (!flight) return;
            var baggage = new Baggage({
                id: msg.EPC,
                code: getCode(msg.flightId),
                start: 0//msg.distance
            });
            flight.get("baggages").add(baggage, {merge: true});
        });
        client.subscribe('/topic/' + VBaggageRemoved, function(message) {
            var msg = getObject(message.body);
            var flight = flightCollection.get(msg.flightId);
            if (!flight) return;
            var baggage = new Baggage({
                id: msg.EPC
            });
            flight.get("baggages").remove(baggage);
        });
        client.subscribe('/topic/' + VFlightUpdated, function(message) {
            var msg = getObject(message.body);
            var flight = new Flight({
                id: msg.flightId,
                number: getDisplayCode(msg.flightId),
                total: msg.total,
                sorted: msg.sorted,
                unsorted: msg.unsorted,
                missing: msg.missing,
                code: getCode(msg.flightId)
            });
            flightCollection.add(flight, {merge: true});
        });
        client.subscribe('/topic/' + VFlightRemoved, function(message) {
            var msg = getObject(message.body);
            var flight = new Flight({
                id: msg.flightId
            });
            flightCollection.remove(flight);
        });
        client.subscribe('/topic/' + VNotificationUpdated, function(obj) {
            var msg = getObject(obj.body);
            var message = new Message({
                id: msg.UUID,
                content: msg.content,
                time: msg.time,
                expire: msg.expire
            });
            if (msg.expire > new Date().getTime())
                messageCollection.add(message, {merge: true});
        });
        client.subscribe('/topic/' + VNotificationRemoved, function(obj) {
            var msg = getObject(message.body);
            var message = new Message({
                id: msg.UUID
            });
            messageCollection.remove(message);
        });
    };

    function startup() {
        setTimeout(function() {
            client.connect(login, passcode, onconnect, startup);
        }, CONNECT_INTERVAL);
    }
    startup();

    setInterval(function() {
        flightView.nextPage();
    }, PAGE_INTERVAL);

    var messageCounter = -1;
    $('#message').text(EMPTY_MESSAGE);
    $('marquee').marquee().on('start', function() {
        if (messageCollection.length == 0) {
            $('#message').text(EMPTY_MESSAGE);
            return;
        }
        if (++messageCounter >= messageCollection.length) {
            messageCounter = 0;
        }
        var message = messageCollection.at(messageCounter);
        var suffix = ' (' + new Date(message.get('time')).format('m月d日H:MM') + ')';
        $('#message').text(message.get('content') + suffix);
    });
}

function test() {

    var msg = {
        flightId: "FLIGHT1/3AUG2012",
        sorted: 20,
        unsorted: 50,
        missing: 20,
        total: 90
    };
    var flight = new Flight({
        id: msg.flightId,
        number: getDisplayCode(msg.flightId),
        total: msg.total,
        sorted: msg.sorted,
        unsorted: msg.unsorted,
        missing: msg.missing,
        code: getCode(msg.flightId)
    });
    flightCollection.add(flight, {merge: true});
    var msg = {
        flightId: "FLIGHT2/3AUG2012",
        sorted: 20,
        unsorted: 50,
        missing: 20,
        total: 90
    };
    var flight = new Flight({
        id: msg.flightId,
        number: getDisplayCode(msg.flightId),
        total: msg.total,
        sorted: msg.sorted,
        unsorted: msg.unsorted,
        missing: msg.missing,
        code: getCode(msg.flightId)
    });
    flightCollection.add(flight, {merge: true});
    var msg = {
        flightId: "FLIGHT3/3AUG2012",
        sorted: 20,
        unsorted: 50,
        missing: 20,
        total: 90
    };
    var flight = new Flight({
        id: msg.flightId,
        number: getDisplayCode(msg.flightId),
        total: msg.total,
        sorted: msg.sorted,
        unsorted: msg.unsorted,
        missing: msg.missing,
        code: getCode(msg.flightId)
    });
    flightCollection.add(flight, {merge: true});
    var msg = {
        flightId: "FLIGHT4/3AUG2012",
        sorted: 20,
        unsorted: 50,
        missing: 20,
        total: 90
    };
    var flight = new Flight({
        id: msg.flightId,
        number: getDisplayCode(msg.flightId),
        total: msg.total,
        sorted: msg.sorted,
        unsorted: msg.unsorted,
        missing: msg.missing,
        code: getCode(msg.flightId)
    });
    flightCollection.add(flight, {merge: true});
    var msg = {
        flightId: "FLIGHT5/3AUG2012",
        sorted: 20,
        unsorted: 50,
        missing: 20,
        total: 90
    };
    var flight = new Flight({
        id: msg.flightId,
        number: getDisplayCode(msg.flightId),
        total: msg.total,
        sorted: msg.sorted,
        unsorted: msg.unsorted,
        missing: msg.missing,
        code: getCode(msg.flightId)
    });
    flightCollection.add(flight, {merge: true});
    var msg = {
        flightId: "FLIGHT6/3AUG2012",
        sorted: 20,
        unsorted: 50,
        missing: 20,
        total: 90
    };
    var flight = new Flight({
        id: msg.flightId,
        number: getDisplayCode(msg.flightId),
        total: msg.total,
        sorted: msg.sorted,
        unsorted: msg.unsorted,
        missing: msg.missing,
        code: getCode(msg.flightId)
    });
    flightCollection.add(flight, {merge: true});
    var msg = {
        flightId: "FLIGHT7/3AUG2012",
        sorted: 20,
        unsorted: 50,
        missing: 20,
        total: 90
    };
    var flight = new Flight({
        id: msg.flightId,
        number: getDisplayCode(msg.flightId),
        total: msg.total,
        sorted: msg.sorted,
        unsorted: msg.unsorted,
        missing: msg.missing,
        code: getCode(msg.flightId)
    });
    flightCollection.add(flight, {merge: true});
    var msg = {
        flightId: "FLIGHT8/3AUG2012",
        sorted: 20,
        unsorted: 50,
        missing: 20,
        total: 90
    };
    var flight = new Flight({
        id: msg.flightId,
        number: getDisplayCode(msg.flightId),
        total: msg.total,
        sorted: msg.sorted,
        unsorted: msg.unsorted,
        missing: msg.missing,
        code: getCode(msg.flightId)
    });
    flightCollection.add(flight, {merge: true});
    var msg = {
        flightId: "FLIGHT9/3AUG2012",
        sorted: 20,
        unsorted: 50,
        missing: 20,
        total: 90
    };
    var flight = new Flight({
        id: msg.flightId,
        number: getDisplayCode(msg.flightId),
        total: msg.total,
        sorted: msg.sorted,
        unsorted: msg.unsorted,
        missing: msg.missing,
        code: getCode(msg.flightId)
    });
    flightCollection.add(flight, {merge: true});
    var msg = {
        flightId: "FLIGH10/3AUG2012",
        sorted: 20,
        unsorted: 50,
        missing: 20,
        total: 90
    };
    var flight = new Flight({
        id: msg.flightId,
        number: getDisplayCode(msg.flightId),
        total: msg.total,
        sorted: msg.sorted,
        unsorted: msg.unsorted,
        missing: msg.missing,
        code: getCode(msg.flightId)
    });
    flightCollection.add(flight, {merge: true});
    var msg = {
        flightId: "FLIGH11/3AUG2012",
        sorted: 20,
        unsorted: 50,
        missing: 20,
        total: 90
    };
    var flight = new Flight({
        id: msg.flightId,
        number: getDisplayCode(msg.flightId),
        total: msg.total,
        sorted: msg.sorted,
        unsorted: msg.unsorted,
        missing: msg.missing,
        code: getCode(msg.flightId)
    });
    flightCollection.add(flight, {merge: true});
    var msg = {
        flightId: "FLIGH12/3AUG2012",
        sorted: 20,
        unsorted: 50,
        missing: 20,
        total: 90
    };
    var flight = new Flight({
        id: msg.flightId,
        number: getDisplayCode(msg.flightId),
        total: msg.total,
        sorted: msg.sorted,
        unsorted: msg.unsorted,
        missing: msg.missing,
        code: getCode(msg.flightId)
    });
    flightCollection.add(flight, {merge: true});
    var msg = {
        flightId: "FLIGH13/3AUG2012",
        sorted: 20,
        unsorted: 50,
        missing: 20,
        total: 90
    };
    var flight = new Flight({
        id: msg.flightId,
        number: getDisplayCode(msg.flightId),
        total: msg.total,
        sorted: msg.sorted,
        unsorted: msg.unsorted,
        missing: msg.missing,
        code: getCode(msg.flightId)
    });
    flightCollection.add(flight, {merge: true});
    var msg = {
        flightId: "FLIGH14/3AUG2012",
        sorted: 20,
        unsorted: 50,
        missing: 20,
        total: 90
    };
    var flight = new Flight({
        id: msg.flightId,
        number: getDisplayCode(msg.flightId),
        total: msg.total,
        sorted: msg.sorted,
        unsorted: msg.unsorted,
        missing: msg.missing,
        code: getCode(msg.flightId)
    });
    flightCollection.add(flight, {merge: true});
    var msg = {
        flightId: "FLIGH15/3AUG2012",
        sorted: 20,
        unsorted: 50,
        missing: 20,
        total: 90
    };
    var flight = new Flight({
        id: msg.flightId,
        number: getDisplayCode(msg.flightId),
        total: msg.total,
        sorted: msg.sorted,
        unsorted: msg.unsorted,
        missing: msg.missing,
        code: getCode(msg.flightId)
    });
    flightCollection.add(flight, {merge: true});
    var msg = {
        flightId: "FLIGH16/3AUG2012",
        sorted: 20,
        unsorted: 50,
        missing: 20,
        total: 90
    };
    var flight = new Flight({
        id: msg.flightId,
        number: getDisplayCode(msg.flightId),
        total: msg.total,
        sorted: msg.sorted,
        unsorted: msg.unsorted,
        missing: msg.missing,
        code: getCode(msg.flightId)
    });
    flightCollection.add(flight, {merge: true});
    var msg = {
        flightId: "FLIGH17/3AUG2012",
        sorted: 20,
        unsorted: 50,
        missing: 20,
        total: 90
    };
    var flight = new Flight({
        id: msg.flightId,
        number: getDisplayCode(msg.flightId),
        total: msg.total,
        sorted: msg.sorted,
        unsorted: msg.unsorted,
        missing: msg.missing,
        code: getCode(msg.flightId)
    });
    flightCollection.add(flight, {merge: true});
    var msg = {
        flightId: "FLIGH18/3AUG2012",
        sorted: 20,
        unsorted: 50,
        missing: 20,
        total: 90
    };
    var flight = new Flight({
        id: msg.flightId,
        number: getDisplayCode(msg.flightId),
        total: msg.total,
        sorted: msg.sorted,
        unsorted: msg.unsorted,
        missing: msg.missing,
        code: getCode(msg.flightId)
    });
    flightCollection.add(flight, {merge: true});
    var msg = {
        flightId: "FLIGH19/3AUG2012",
        sorted: 20,
        unsorted: 50,
        missing: 20,
        total: 90
    };
    var flight = new Flight({
        id: msg.flightId,
        number: getDisplayCode(msg.flightId),
        total: msg.total,
        sorted: msg.sorted,
        unsorted: msg.unsorted,
        missing: msg.missing,
        code: getCode(msg.flightId)
    });
    flightCollection.add(flight, {merge: true});
    var msg = {
        flightId: "FLIGH20/3AUG2012",
        sorted: 20,
        unsorted: 50,
        missing: 20,
        total: 90
    };
    var flight = new Flight({
        id: msg.flightId,
        number: getDisplayCode(msg.flightId),
        total: msg.total,
        sorted: msg.sorted,
        unsorted: msg.unsorted,
        missing: msg.missing,
        code: getCode(msg.flightId)
    });
    flightCollection.add(flight, {merge: true});
    var msg = {
        flightId: "FLIGH21/3AUG2012",
        sorted: 20,
        unsorted: 50,
        missing: 20,
        total: 90
    };
    var flight = new Flight({
        id: msg.flightId,
        number: getDisplayCode(msg.flightId),
        total: msg.total,
        sorted: msg.sorted,
        unsorted: msg.unsorted,
        missing: msg.missing,
        code: getCode(msg.flightId)
    });
    flightCollection.add(flight, {merge: true});
    var msg = {
        flightId: "FLIGH22/3AUG2012",
        sorted: 20,
        unsorted: 50,
        missing: 20,
        total: 90
    };
    var flight = new Flight({
        id: msg.flightId,
        number: getDisplayCode(msg.flightId),
        total: msg.total,
        sorted: msg.sorted,
        unsorted: msg.unsorted,
        missing: msg.missing,
        code: getCode(msg.flightId)
    });
    flightCollection.add(flight, {merge: true});
    var msg = {
        flightId: "FLIGH23/3AUG2012",
        sorted: 20,
        unsorted: 50,
        missing: 20,
        total: 90
    };
    var flight = new Flight({
        id: msg.flightId,
        number: getDisplayCode(msg.flightId),
        total: msg.total,
        sorted: msg.sorted,
        unsorted: msg.unsorted,
        missing: msg.missing,
        code: getCode(msg.flightId)
    });
    flightCollection.add(flight, {merge: true});
    var msg = {
        flightId: "FLIGH24/03AUG2012",
        sorted: 20,
        unsorted: 50,
        missing: 20,
        total: 90
    };
    var flight = new Flight({
        id: msg.flightId,
        number: getDisplayCode(msg.flightId),
        total: msg.total,
        sorted: msg.sorted,
        unsorted: msg.unsorted,
        missing: msg.missing,
        code: getCode(msg.flightId)
    });
    flightCollection.add(flight, {merge: true});
    msg = {
        flightId: "FLIGHT1/3AUG2012",
        EPC: "DS324252",
        distance: 0
    };
    var baggage = new Baggage({
        id: msg.EPC,
        code: getCode(msg.flightId),
        start: msg.distance
    });
    flight.get("baggages").add(baggage, {merge: true});
    msg = {
        flightId: "FLIGHT2/3AUG2012",
        EPC: "DS3242352",
        distance: 0.1
    };
    baggage = new Baggage({
        id: msg.EPC,
        code: getCode(msg.flightId),
        start: msg.distance
    });
    flight.get("baggages").add(baggage, {merge: true});
    msg = {
        flightId: "FLIGHT3/3AUG2012",
        EPC: "DS324223452",
        distance: 0.2
    };
    baggage = new Baggage({
        id: msg.EPC,
        code: getCode(msg.flightId),
        start: msg.distance
    });
    flight.get("baggages").add(baggage, {merge: true});
    msg = {
        flightId: "FLIGHT4/3AUG2012",
        EPC: "DS324234252",
        distance: 0.3
    };
    baggage = new Baggage({
        id: msg.EPC,
        code: getCode(msg.flightId),
        start: msg.distance
    });
    flight.get("baggages").add(baggage, {merge: true});
    msg = {
        flightId: "FLIGHT5/3AUG2012",
        EPC: "DS32424352",
        distance: 0.4
    };
    baggage = new Baggage({
        id: msg.EPC,
        code: getCode(msg.flightId),
        start: msg.distance
    });
    flight.get("baggages").add(baggage, {merge: true});
    msg = {
        flightId: "FLIGHT6/3AUG2012",
        EPC: "DS3242352gs",
        distance: 0.5
    };
    baggage = new Baggage({
        id: msg.EPC,
        code: getCode(msg.flightId),
        start: msg.distance
    });
    flight.get("baggages").add(baggage, {merge: true});
    msg = {
        flightId: "FLIGHT7/3AUG2012",
        EPC: "DSbs42352",
        distance: 0.6
    };
    baggage = new Baggage({
        id: msg.EPC,
        code: getCode(msg.flightId),
        start: msg.distance
    });
    flight.get("baggages").add(baggage, {merge: true});
    msg = {
        flightId: "FLIGHT8/3AUG2012",
        EPC: "DS32423xzv52",
        distance: 0.7
    };
    baggage = new Baggage({
        id: msg.EPC,
        code: getCode(msg.flightId),
        start: msg.distance
    });
    flight.get("baggages").add(baggage, {merge: true});
    msg = {
        flightId: "FLIGHT9/3AUG2012",
        EPC: "DS322423xzv52",
        distance: 0.8
    };
    baggage = new Baggage({
        id: msg.EPC,
        code: getCode(msg.flightId),
        start: msg.distance
    });
    flight.get("baggages").add(baggage, {merge: true});
    msg = {
        flightId: "FLIGH10/3AUG2012",
        EPC: "DS32423x5zv52",
        distance: 0.9
    };
    baggage = new Baggage({
        id: msg.EPC,
        code: getCode(msg.flightId),
        start: msg.distance
    });
    flight.get("baggages").add(baggage, {merge: true});

    messageCollection.add(new Message({
        id: "2352-235235",
        content: "hi!",
        time: new Date().getTime(),
        expire: new Date().getTime() + 100000
    }));
}

buildClock();
buildClient();
//test();