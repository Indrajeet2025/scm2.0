console.log("contacts....")
const viewContactModal=document.getElementById("view_contact_modal");

const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

// instance options object
const instanceOptions = {
  id: 'view_contact_modal',
  override: true
};

const contactModal=new Modal(view_contact_modal,options,instanceOptions);

function openContactModal()
{
  contactModal.show();
}
function closeContactModal()
{
  contactModal.hide();
}

    async function loadContactData(id) {
        try {
            console.log("ID =", id);
            const response = await fetch(`http://localhost:8080/api/contacts/${id}`);
            const data = await response.json();
            console.log("Contact =", data);
            // Fill modal fields
            document.querySelector('#contact_name').textContent = data.name || 'Unknown contact';
            document.querySelector('#contact_tagline').textContent = data.email || '';

            document.querySelector('#contact_email').textContent = data.email || 'not available';
            document.querySelector('#contact_phone').textContent = data.phoneNumber || 'not available';
            document.querySelector('#contact_address').textContent = data.address || 'not available';
            document.querySelector('#contact_description').textContent = data.description || '—';
            // Image
            const img = document.querySelector('#contact_image');
            if (data.picture) {
                img.src = data.picture;
            } else {
                img.src = '/images/defaultProfile.jpg';
            }
            // Social links
            const fb = document.querySelector('#contact_facebook');
            const li = document.querySelector('#contact_linkedin');

            if (data.facebookLink) {
                fb.href = data.facebookLink;
                fb.classList.remove('pointer-events-none', 'opacity-50');
            } else {
                fb.href = '#';
                fb.classList.add('pointer-events-none', 'opacity-50');
            }

            if (data.linkedInLink) {
                li.href = data.linkedInLink;
                li.classList.remove('pointer-events-none', 'opacity-50');
            } else {
                li.href = '#';
                li.classList.add('pointer-events-none', 'opacity-50');
            }

            // Favourite status
            const favIcon = document.querySelector('#contact_favorite_icon');
            const favText = document.querySelector('#contact_favorite_text');

            if (data.favorite) {
                favIcon.classList.remove('fa-regular');
                favIcon.classList.add('fa-solid');
                favText.textContent = 'Marked as favourite';
            } else {
                favIcon.classList.remove('fa-solid');
                favIcon.classList.add('fa-regular');
                favText.textContent = 'Not marked as favourite';
            }

            // Finally open modal
            openContactModal();

            return data;
        } catch (error) {
            console.log("Error:", error);
        }
    }

async function toggleFavorite(id, btn) {
    try {
        const response = await fetch(`/api/contacts/${id}/favorite`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
                // if you enable CSRF later, we’ll add X-CSRF-TOKEN here
            }
        });

        if (!response.ok) {
            throw new Error('Failed to toggle favourite');
        }

        const data = await response.json();
        console.log("Favorite toggled:", data);

        // Update star icon in this row
        const icon = btn.querySelector('i');
        if (!icon) return;

        if (data.favorite) {
            icon.classList.remove('fa-regular', 'text-gray-400');
            icon.classList.add('fa-solid', 'text-yellow-400');
        } else {
            icon.classList.remove('fa-solid', 'text-yellow-400');
            icon.classList.add('fa-regular', 'text-gray-400');
        }

    } catch (error) {
        console.error("Error toggling favourite:", error);    }
}

//delete contact
function deleteContactData(contactId) {

    // Detect dark mode dynamically
    const isDark = document.documentElement.classList.contains("dark");

    Swal.fire({
        title: "Delete Contact?",
        text: "This contact will be permanently removed.",
        icon: "warning",
        showCancelButton: true,

        confirmButtonText: "Yes, delete",
        cancelButtonText: "Cancel",

        // Button Colors (matches Tailwind + your brand)
        confirmButtonColor: "#dc2626",   // red-600
        cancelButtonColor: isDark ? "#4b5563" : "#6b7280", // gray-600/500

        // Popup theme based on Dark/Light mode
        background: isDark ? "#1f2937" : "#ffffff", // dark:bg-gray-800 / white
        color: isDark ? "#e5e7eb" : "#111827",      // text-gray-300 / text-gray-900

        // Smooth rounded edges (matches your UI cards)
        customClass: {
            popup: "rounded-xl shadow-lg",
            confirmButton: "rounded-base px-4 py-2",
            cancelButton: "rounded-base px-4 py-2"
        }

    }).then((result) => {

        if (result.isConfirmed) {
            // Redirect to ContactController delete mapping
            window.location.href = `/user/contacts/delete/${contactId}`;
        }
    });
}


