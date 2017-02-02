// It's an abstract kind of feel
!function(root) {
    var $doc = root.document;
    var _ = {};

    // Helpers
    var _array = function(nodes) { return [].slice.call(nodes); };

    // DOMReady listener
    root.dom = function(func) {
        $doc.addEventListener('DOMContentLoaded', function() {
            func(_);
        });
    };

    _.hide = function(e) { e.classList.add('hide'); };
    _.show = function(e) { e.classList.remove('hide'); };

    // Selectors
    _.element = $doc.querySelector.bind($doc);
    _.elements = function(selector, parent) {
        return _array((parent || $doc).querySelectorAll(selector));
    };

    _.cons = function(type, props, children) {
        if(children && ! Array.isArray(children))
            children = [children];

        var newnode = $doc.createElement(type);

        if( props )
            Object.keys(props).forEach( function(prop) {
                newnode.setAttribute(prop, props[prop]);
            });

        if( children )
            children.forEach( function(child) {
                newnode.appendChild('object' === typeof child ? child : $doc.createTextNode(child));
            });

        return newnode;
    };

    _.on = function(node, event, callback) {
        node.addEventListener(event, callback);
    };

    _.params = function(data) {
        return Object.keys(data)
            .map(function(key) {
                return encodeURIComponent(key) + '=' + encodeURIComponent(data[key]);
            })
            .join('&');
    };

}(window);
