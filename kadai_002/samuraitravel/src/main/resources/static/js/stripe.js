const stripe = Stripe('pk_test_51QNAisJe6vN6aWPkDhORLaog4shYJZtu1PC7DieIniPCrmIdA8mCJDCZcauUNByaNq2sqvYXth9zFeiPTa791cAe00FlagxU7R');
const paymentButton = document.querySelector('#paymentButton');

paymentButton.addEventListener('click', () => {
	stripe.redirectToCheckout({
		sessionId: sessionId
	})
})